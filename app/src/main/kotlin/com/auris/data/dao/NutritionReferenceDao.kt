package com.auris.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.auris.data.entity.NutritionReferenceEntity
import com.auris.domain.model.NutrientId
import kotlinx.coroutines.flow.Flow

/**
 * NutritionReferenceDao — Room DAO for the static nutrient reference table.
 * Read-only after initial seed.
 */
@Dao
interface NutritionReferenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(refs: List<NutritionReferenceEntity>)

    @Query("SELECT * FROM nutrition_reference ORDER BY nutrient_id ASC")
    fun getAll(): Flow<List<NutritionReferenceEntity>>

    @Query("SELECT * FROM nutrition_reference WHERE nutrient_id = :nutrientId")
    suspend fun getByNutrientId(nutrientId: NutrientId): NutritionReferenceEntity?

    @Query("SELECT COUNT(*) FROM nutrition_reference")
    suspend fun count(): Int
}
