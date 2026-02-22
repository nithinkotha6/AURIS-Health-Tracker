package com.auris.data.repository

import com.auris.data.dao.HabitCompletionDao
import com.auris.data.dao.HabitDao
import com.auris.data.entity.HabitCompletionEntity
import com.auris.data.entity.HabitEntity
import com.auris.domain.model.Habit
import com.auris.domain.model.HabitRecurrence
import com.auris.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val habitCompletionDao: HabitCompletionDao
) : HabitRepository {

    override fun getAllHabits(): Flow<List<Habit>> =
        habitDao.getAll().map { list -> list.map { Habit.fromEntity(it) } }

    override suspend fun addHabit(habit: Habit) {
        val now = System.currentTimeMillis()
        habitDao.insert(
            HabitEntity(
                habitId = habit.habitId,
                name = habit.name,
                iconName = habit.iconName,
                colorHex = habit.colorHex,
                recurrence = habit.recurrence,
                reminderMinutes = habit.reminderMinutes,
                streakCurrent = 0,
                streakBest = 0,
                createdAt = now,
                updatedAt = now
            )
        )
    }

    override suspend fun updateHabit(habit: Habit) {
        val e = habitDao.getById(habit.habitId) ?: return
        habitDao.update(
            e.copy(
                name = habit.name,
                iconName = habit.iconName,
                colorHex = habit.colorHex,
                recurrence = habit.recurrence,
                reminderMinutes = habit.reminderMinutes,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun deleteHabit(habitId: String) {
        habitDao.deleteById(habitId)
    }

    override suspend fun completeHabit(habitId: String, date: LocalDate) {
        habitCompletionDao.insert(
            HabitCompletionEntity(
                completionId = UUID.randomUUID().toString(),
                habitId = habitId,
                date = date,
                completedAt = System.currentTimeMillis()
            )
        )
    }
}
