package com.auris.data.di

import android.content.Context
import androidx.room.Room
import com.auris.data.dao.FoodEntryDao
import com.auris.data.dao.HabitCompletionDao
import com.auris.data.dao.HabitDao
import com.auris.data.dao.NutritionReferenceDao
import com.auris.data.dao.UserBadgeDao
import com.auris.data.dao.VitaminLogDao
import com.auris.data.dao.WellbeingLogDao
import com.auris.data.database.AurisDatabase
import com.auris.data.database.seed.NutritionReferenceSeed
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

/**
 * DatabaseModule — Hilt module providing Room database instance and all DAOs.
 *
 * Phase 6: standard (unencrypted) Room.
 * Future: add SQLCipher SupportFactory for encrypted DB.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        nutritionReferenceDaoProvider: Provider<NutritionReferenceDao>
    ): AurisDatabase {
        return Room.databaseBuilder(
            context,
            AurisDatabase::class.java,
            "auris_database"
        )
            .addCallback(NutritionReferenceSeed(nutritionReferenceDaoProvider))
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFoodEntryDao(db: AurisDatabase): FoodEntryDao = db.foodEntryDao()

    @Provides
    fun provideVitaminLogDao(db: AurisDatabase): VitaminLogDao = db.vitaminLogDao()

    @Provides
    fun provideNutritionReferenceDao(db: AurisDatabase): NutritionReferenceDao = db.nutritionReferenceDao()

    @Provides
    fun provideHabitDao(db: AurisDatabase): HabitDao = db.habitDao()

    @Provides
    fun provideHabitCompletionDao(db: AurisDatabase): HabitCompletionDao = db.habitCompletionDao()

    @Provides
    fun provideUserBadgeDao(db: AurisDatabase): UserBadgeDao = db.userBadgeDao()

    @Provides
    fun provideWellbeingLogDao(db: AurisDatabase): WellbeingLogDao = db.wellbeingLogDao()
}
