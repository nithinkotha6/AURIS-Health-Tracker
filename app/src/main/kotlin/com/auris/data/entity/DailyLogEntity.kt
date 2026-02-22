package com.auris.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * DailyLogEntity — aggregation entity for daily macro totals.
 *
 * Provides a quick summary without scanning all food entries.
 * Updated whenever food entries change for the corresponding date.
 */
@Entity(
    tableName = "daily_logs",
    indices = [Index(value = ["date"], unique = true)]
)
data class DailyLogEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "log_id")
    val logId: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: Long = 1L,

    @ColumnInfo(name = "date")
    val date: LocalDate,

    @ColumnInfo(name = "total_calories")
    val totalCalories: Float = 0f,

    @ColumnInfo(name = "total_protein_g")
    val totalProteinG: Float = 0f,

    @ColumnInfo(name = "total_carbs_g")
    val totalCarbsG: Float = 0f,

    @ColumnInfo(name = "total_fat_g")
    val totalFatG: Float = 0f
)
