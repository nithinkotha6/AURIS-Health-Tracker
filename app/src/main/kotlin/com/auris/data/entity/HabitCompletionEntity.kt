package com.auris.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * HabitCompletionEntity — one completion record (Phase 11).
 * One row per habit per day when user marks complete.
 */
@Entity(
    tableName = "habit_completions",
    indices = [Index(value = ["habit_id", "date"], unique = true)]
)
data class HabitCompletionEntity(
    @PrimaryKey
    @ColumnInfo(name = "completion_id")
    val completionId: String,

    @ColumnInfo(name = "habit_id")
    val habitId: String,

    @ColumnInfo(name = "date")
    val date: LocalDate,

    @ColumnInfo(name = "completed_at")
    val completedAt: Long = System.currentTimeMillis()
)
