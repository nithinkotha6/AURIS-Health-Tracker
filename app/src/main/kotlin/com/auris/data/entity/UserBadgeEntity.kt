package com.auris.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * UserBadgeEntity — earned badge (Phase 11).
 */
@Entity(tableName = "user_badges")
data class UserBadgeEntity(
    @PrimaryKey
    @ColumnInfo(name = "badge_id")
    val badgeId: String,

    @ColumnInfo(name = "badge_type")
    val badgeType: String,

    @ColumnInfo(name = "habit_id")
    val habitId: String? = null,

    @ColumnInfo(name = "earned_at")
    val earnedAt: Long = System.currentTimeMillis()
)
