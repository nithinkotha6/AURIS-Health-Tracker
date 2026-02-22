package com.auris.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.auris.data.entity.FoodEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * FoodEntryDao — Room DAO for food log CRUD operations.
 */
@Dao
interface FoodEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: FoodEntryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<FoodEntryEntity>)

    @Delete
    suspend fun delete(entry: FoodEntryEntity)

    @Query("DELETE FROM food_entries WHERE entry_id = :entryId")
    suspend fun deleteById(entryId: Long)

    @Query("SELECT * FROM food_entries WHERE date = :date ORDER BY logged_at DESC")
    fun getByDate(date: LocalDate): Flow<List<FoodEntryEntity>>

    @Query("SELECT * FROM food_entries WHERE date = :date ORDER BY logged_at DESC")
    suspend fun getByDateOnce(date: LocalDate): List<FoodEntryEntity>

    @Query("SELECT * FROM food_entries ORDER BY logged_at DESC LIMIT :limit")
    fun getRecent(limit: Int = 50): Flow<List<FoodEntryEntity>>

    @Query("SELECT COUNT(*) FROM food_entries WHERE date = :date")
    suspend fun countByDate(date: LocalDate): Int
}
