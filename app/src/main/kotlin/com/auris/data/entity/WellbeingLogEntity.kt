package com.auris.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * WellbeingLogEntity — daily energy/wellbeing score (Phase 12).
 * 1=Low, 2=Okay, 3=Great.
 */
@Entity(tableName = "wellbeing_log")
data class WellbeingLogEntity(
    @PrimaryKey
    @ColumnInfo(name = "wellbeing_id")
    val wellbeingId: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "energy_score")
    val energyScore: Int,

    @ColumnInfo(name = "note")
    val note: String? = null,

    @ColumnInfo(name = "logged_at")
    val loggedAt: Long = System.currentTimeMillis()
)
