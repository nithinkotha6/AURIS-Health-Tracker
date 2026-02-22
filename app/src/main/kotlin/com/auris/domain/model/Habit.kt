package com.auris.domain.model

import com.auris.data.entity.HabitEntity

/** Domain model for a habit (Phase 11). */
data class Habit(
    val habitId: String,
    val name: String,
    val iconName: String,
    val colorHex: String,
    val recurrence: HabitRecurrence,
    val reminderMinutes: Int?,
    val streakCurrent: Int,
    val streakBest: Int
) {
    companion object {
        fun fromEntity(e: HabitEntity): Habit = Habit(
            habitId = e.habitId,
            name = e.name,
            iconName = e.iconName,
            colorHex = e.colorHex,
            recurrence = e.recurrence,
            reminderMinutes = e.reminderMinutes,
            streakCurrent = e.streakCurrent,
            streakBest = e.streakBest
        )
    }
}
