package com.auris.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.auris.domain.usecase.SyncHealthConnectUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * HealthConnectSyncWorker — optional periodic sync with Health Connect.
 *
 * Phase 10:
 * - Can be scheduled via WorkManager (e.g., every 15 minutes) to keep
 *   burn-adjusted RDAs up to date even when the app is not in foreground.
 */
@HiltWorker
class HealthConnectSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncHealthConnectUseCase: SyncHealthConnectUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            syncHealthConnectUseCase()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

