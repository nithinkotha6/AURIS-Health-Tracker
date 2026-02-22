package com.auris.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.auris.data.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity)

    @Update
    suspend fun update(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE habit_id = :habitId")
    suspend fun deleteById(habitId: String)

    @Query("SELECT * FROM habits ORDER BY created_at ASC")
    fun getAll(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE habit_id = :habitId")
    suspend fun getById(habitId: String): HabitEntity?
}
