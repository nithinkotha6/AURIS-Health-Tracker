package com.auris.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.auris.data.dao.FoodEntryDao
import com.auris.data.dao.HabitCompletionDao
import com.auris.data.dao.HabitDao
import com.auris.data.dao.NutritionReferenceDao
import com.auris.data.dao.UserBadgeDao
import com.auris.data.dao.VitaminLogDao
import com.auris.data.dao.WellbeingLogDao
import com.auris.data.entity.DailyLogEntity
import com.auris.data.entity.FoodEntryEntity
import com.auris.data.entity.HabitCompletionEntity
import com.auris.data.entity.HabitEntity
import com.auris.data.entity.NutritionReferenceEntity
import com.auris.data.entity.UserBadgeEntity
import com.auris.data.entity.UserProfileEntity
import com.auris.data.entity.VitaminLogEntity
import com.auris.data.entity.WellbeingLogEntity

/**
 * AurisDatabase — Room database with all AURIS entities.
 *
 * Version 1 — initial schema (Phase 6).
 * Version 2 — habits, habit_completions, user_badges (Phase 11).
 * Version 3 — wellbeing_log (Phase 12).
 */
@Database(
    entities = [
        FoodEntryEntity::class,
        VitaminLogEntity::class,
        NutritionReferenceEntity::class,
        DailyLogEntity::class,
        UserProfileEntity::class,
        HabitEntity::class,
        HabitCompletionEntity::class,
        UserBadgeEntity::class,
        WellbeingLogEntity::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AurisDatabase : RoomDatabase() {
    abstract fun foodEntryDao(): FoodEntryDao
    abstract fun vitaminLogDao(): VitaminLogDao
    abstract fun nutritionReferenceDao(): NutritionReferenceDao
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionDao(): HabitCompletionDao
    abstract fun userBadgeDao(): UserBadgeDao
    abstract fun wellbeingLogDao(): WellbeingLogDao
}
