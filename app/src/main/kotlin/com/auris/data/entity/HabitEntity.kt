package com.auris.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.auris.domain.model.HabitRecurrence
import java.time.LocalTime

/**
 * HabitEntity — one habit definition (Phase 11).
 */
@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey
    @ColumnInfo(name = "habit_id")
    val habitId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "icon_name")
    val iconName: String = "check_circle",

    @ColumnInfo(name = "color_hex")
    val colorHex: String = "#007AFF",

    @ColumnInfo(name = "recurrence")
    val recurrence: HabitRecurrence,

    @ColumnInfo(name = "reminder_minutes")
    val reminderMinutes: Int? = null,

    @ColumnInfo(name = "streak_current")
    val streakCurrent: Int = 0,

    @ColumnInfo(name = "streak_best")
    val streakBest: Int = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
