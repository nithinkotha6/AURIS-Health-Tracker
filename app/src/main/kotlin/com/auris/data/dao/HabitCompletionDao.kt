package com.auris.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.auris.data.entity.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitCompletionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(completion: HabitCompletionEntity)

    @Query("SELECT * FROM habit_completions WHERE habit_id = :habitId AND date = :date LIMIT 1")
    suspend fun getByHabitAndDate(habitId: String, date: LocalDate): HabitCompletionEntity?

    @Query("SELECT * FROM habit_completions WHERE date = :date")
    suspend fun getByDate(date: LocalDate): List<HabitCompletionEntity>

    @Query("SELECT * FROM habit_completions WHERE habit_id = :habitId ORDER BY date DESC LIMIT :days")
    fun getByHabitLastDays(habitId: String, days: Int): Flow<List<HabitCompletionEntity>>
}
