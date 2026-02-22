package com.auris.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.auris.data.dao.HabitCompletionDao
import com.auris.data.dao.HabitDao
import com.auris.data.entity.HabitEntity
import com.auris.domain.model.HabitRecurrence
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * HabitStreakWorker — runs nightly to update streak_current and streak_best (Phase 11).
 * For each habit, checks if yesterday matched recurrence and had a completion;
 * updates streak and optionally awards badges.
 */
@HiltWorker
class HabitStreakWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val habitDao: HabitDao,
    private val habitCompletionDao: HabitCompletionDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val yesterday = LocalDate.now().minusDays(1)
            val habits = habitDao.getAll().first()
            for (habit in habits) {
                if (!recurrenceMatches(habit.recurrence, yesterday)) continue
                val completed = habitCompletionDao.getByHabitAndDate(habit.habitId, yesterday) != null
                val newCurrent = if (completed) habit.streakCurrent + 1 else 0
                val newBest = maxOf(habit.streakBest, newCurrent)
                habitDao.update(
                    habit.copy(
                        streakCurrent = newCurrent,
                        streakBest = newBest,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun recurrenceMatches(r: HabitRecurrence, date: LocalDate): Boolean = when (r) {
        HabitRecurrence.DAILY -> true
        HabitRecurrence.WEEKDAYS -> date.dayOfWeek in listOf(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
        )
        HabitRecurrence.WEEKENDS -> date.dayOfWeek in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
        HabitRecurrence.CUSTOM -> true
    }
}
