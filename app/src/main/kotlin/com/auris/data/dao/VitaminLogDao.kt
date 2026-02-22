package com.auris.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.auris.data.entity.VitaminLogEntity
import com.auris.domain.model.NutrientId
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * VitaminLogDao — Room DAO for vitamin status CRUD operations.
 */
@Dao
interface VitaminLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: VitaminLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entries: List<VitaminLogEntity>)

    @Query("SELECT * FROM vitamin_logs WHERE date = :date ORDER BY nutrient_id ASC")
    fun getByDate(date: LocalDate): Flow<List<VitaminLogEntity>>

    @Query("SELECT * FROM vitamin_logs WHERE date = :date ORDER BY nutrient_id ASC")
    suspend fun getByDateOnce(date: LocalDate): List<VitaminLogEntity>

    @Query("SELECT * FROM vitamin_logs WHERE nutrient_id = :nutrientId ORDER BY date DESC LIMIT :days")
    fun getHistory(nutrientId: NutrientId, days: Int = 7): Flow<List<VitaminLogEntity>>

    /** Last 7 days of logs for all nutrients (oldest first). Phase 11 Overview charts. */
    @Query("SELECT * FROM vitamin_logs WHERE date >= :fromDate ORDER BY date ASC, nutrient_id ASC")
    suspend fun getFromDateOnce(fromDate: LocalDate): List<VitaminLogEntity>

    @Query("DELETE FROM vitamin_logs WHERE date = :date")
    suspend fun deleteByDate(date: LocalDate)
}
