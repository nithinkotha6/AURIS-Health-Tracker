package com.auris.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.auris.data.entity.WellbeingLogEntity

/**
 * WellbeingLogDao — Phase 12 wellbeing energy log (1=Low, 2=Okay, 3=Great).
 */
@Dao
interface WellbeingLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WellbeingLogEntity)

    @Query("SELECT * FROM wellbeing_log WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): WellbeingLogEntity?

    @Query("SELECT * FROM wellbeing_log ORDER BY date DESC LIMIT :limit")
    suspend fun getRecent(limit: Int): List<WellbeingLogEntity>
}
