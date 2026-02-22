package com.auris.domain.usecase

import com.auris.domain.repository.HealthConnectRepository
import com.auris.domain.repository.VitaminRepository
import javax.inject.Inject

/**
 * SyncHealthConnectUseCase — reads daily activity/sleep metrics from Health Connect
 * and applies burn adjustments to today's vitamin log.
 *
 * Phase 10:
 * - Called on app foreground (Home screen) to obtain the latest
 *   [ApplyBurnAdjustmentsUseCase.BurnData].
 * - When data is available, triggers [VitaminRepository.recalculate] so that
 *   adjusted RDAs are persisted to Room and reflected in all vitamin streams.
 */
class SyncHealthConnectUseCase @Inject constructor(
    private val healthConnectRepository: HealthConnectRepository,
    private val vitaminRepository: VitaminRepository
) {

    /**
     * @return The latest burn data when Health Connect is available and
     *         permissions are granted; null otherwise.
     */
    suspend operator fun invoke(): ApplyBurnAdjustmentsUseCase.BurnData? {
        val burn = healthConnectRepository.readTodayBurnData() ?: return null
        vitaminRepository.recalculate(burn)
        return burn
    }
}


