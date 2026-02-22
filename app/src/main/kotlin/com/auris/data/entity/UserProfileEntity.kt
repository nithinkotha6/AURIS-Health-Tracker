package com.auris.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * UserProfileEntity — stub user profile for Phase 6.
 *
 * Pre-populated with defaults. Full user profile editing comes in Phase 12.
 */
@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: Long = 1L,

    @ColumnInfo(name = "display_name")
    val displayName: String = "User",

    @ColumnInfo(name = "is_male")
    val isMale: Boolean = true,

    @ColumnInfo(name = "weight_kg")
    val weightKg: Float = 75f,

    @ColumnInfo(name = "height_cm")
    val heightCm: Float = 175f,

    @ColumnInfo(name = "calorie_goal")
    val calorieGoal: Int = 2500,

    @ColumnInfo(name = "protein_goal_g")
    val proteinGoalG: Int = 150,

    @ColumnInfo(name = "carbs_goal_g")
    val carbsGoalG: Int = 280,

    @ColumnInfo(name = "fat_goal_g")
    val fatGoalG: Int = 65
)
