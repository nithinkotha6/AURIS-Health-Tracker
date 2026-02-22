package com.auris.data.health

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.auris.domain.repository.HealthConnectRepository
import com.auris.domain.usecase.ApplyBurnAdjustmentsUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * HealthConnectRepositoryImpl — Health Connect backed implementation of [HealthConnectRepository].
 *
 * Phase 10:
 * - Uses AndroidX Health Connect client to read daily activity metrics.
 * - Gracefully returns null when Health Connect is unavailable or permissions
 *   have not been granted by the user.
 */
@Singleton
class HealthConnectRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : HealthConnectRepository {

    private val zoneId: ZoneId = ZoneId.systemDefault()

    private fun requiredPermissions() = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class)
    )

    override suspend fun hasAllPermissions(): Boolean {
        val sdkStatus = HealthConnectClient.getSdkStatus(context)
        if (sdkStatus != HealthConnectClient.SDK_AVAILABLE) {
            return false
        }

        val client = HealthConnectClient.getOrCreate(context)
        val granted = client.permissionController.getGrantedPermissions()
        return granted.containsAll(requiredPermissions())
    }

    override suspend fun readTodayBurnData(): ApplyBurnAdjustmentsUseCase.BurnData? {
        // 1) Ensure Health Connect is available on this device
        val sdkStatus = HealthConnectClient.getSdkStatus(context)
        if (sdkStatus != HealthConnectClient.SDK_AVAILABLE) {
            return null
        }

        val client = HealthConnectClient.getOrCreate(context)

        // 2) Ensure required permissions are granted
        val granted = client.permissionController.getGrantedPermissions()
        if (!granted.containsAll(requiredPermissions())) {
            // Caller (Profile/Permission flow) is responsible for requesting permissions.
            return null
        }

        // 3) Compute today's time range in device local time
        val now: ZonedDateTime = ZonedDateTime.now(zoneId)
        val startOfDay: ZonedDateTime = now.toLocalDate().atStartOfDay(zoneId)
        val startInstant: Instant = startOfDay.toInstant()
        val endInstant: Instant = now.toInstant()
        val todayRange = TimeRangeFilter.between(startInstant, endInstant)

        // 4) Steps
        val stepsResponse = client.readRecords(
            ReadRecordsRequest(
                recordType = StepsRecord::class,
                timeRangeFilter = todayRange
            )
        )
        val steps = stepsResponse.records.sumOf { it.count }.toInt()

        // 5) Active calories (kcal)
        val caloriesResponse = client.readRecords(
            ReadRecordsRequest(
                recordType = ActiveCaloriesBurnedRecord::class,
                timeRangeFilter = todayRange
            )
        )
        val activeCalories = caloriesResponse.records
            .sumOf { it.energy.inKilocalories }
            .toFloat()

        // 6) Last night's total sleep hours
        val sleepHours = readLastNightSleepHours(client, now) ?: 8f

        return ApplyBurnAdjustmentsUseCase.BurnData(
            activeCalories = activeCalories,
            steps = steps,
            sleepHours = sleepHours
        )
    }

    private suspend fun readLastNightSleepHours(
        client: HealthConnectClient,
        now: ZonedDateTime
    ): Float? {
        // Define \"last night\" as the sleep period between yesterday 18:00 and today 12:00.
        val yesterdayEvening = now.minusDays(1).withHour(18).withMinute(0)
            .withSecond(0).withNano(0)
        val todayNoon = now.withHour(12).withMinute(0).withSecond(0).withNano(0)

        val range = TimeRangeFilter.between(
            yesterdayEvening.toInstant(),
            todayNoon.toInstant()
        )

        val sleepResponse = client.readRecords(
            ReadRecordsRequest(
                recordType = SleepSessionRecord::class,
                timeRangeFilter = range
            )
        )

        if (sleepResponse.records.isEmpty()) return null

        val totalMillis = sleepResponse.records.fold(0L) { acc, record ->
            acc + Duration.between(record.startTime, record.endTime).toMillis()
        }

        return (totalMillis / 1000f / 60f / 60f).takeIf { it > 0f }
    }
}
