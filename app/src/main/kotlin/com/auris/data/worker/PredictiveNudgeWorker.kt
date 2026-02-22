package com.auris.data.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.auris.R
import com.auris.domain.usecase.PredictiveNudgeUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * PredictiveNudgeWorker — nightly background job to analyze trends
 * and post a nutrient nudge notification.
 */
@HiltWorker
class PredictiveNudgeWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val nudgeUseCase: PredictiveNudgeUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val nudge = nudgeUseCase.calculateNudge()
            
            if (nudge != null) {
                showNotification(nudge.message)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun showNotification(message: String) {
        val builder = NotificationCompat.Builder(applicationContext, "AURIS_ALERTS")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("AURIS Health Nudge")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        try {
            // In a real app, check for POST_NOTIFICATIONS permission on Android 13+
            NotificationManagerCompat.from(applicationContext).notify(1001, builder.build())
        } catch (_: SecurityException) {}
    }
}
