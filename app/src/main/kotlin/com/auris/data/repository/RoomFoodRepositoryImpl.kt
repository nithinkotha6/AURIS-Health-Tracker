package com.auris.data.repository

import com.auris.data.dao.FoodEntryDao
import com.auris.data.entity.FoodEntryEntity
import com.auris.domain.model.ParsedFoodItem
import com.auris.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * RoomFoodRepositoryImpl — Room-backed implementation of [FoodRepository].
 *
 * Phase 6: replaces [FakeFoodRepositoryImpl].
 * All reads return reactive Flows from Room DAO.
 */
@Singleton
class RoomFoodRepositoryImpl @Inject constructor(
    private val dao: FoodEntryDao
) : FoodRepository {

    override suspend fun logFood(item: ParsedFoodItem) {
        val entity = FoodEntryEntity.fromDomain(item)
        dao.insert(entity)
    }

    override fun getTodayFoodLog(): Flow<List<ParsedFoodItem>> {
        val today = LocalDate.now()
        return dao.getByDate(today).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getFoodLogByDate(date: LocalDate): Flow<List<ParsedFoodItem>> {
        return dao.getByDate(date).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteFood(id: String) {
        val entryId = id.toLongOrNull() ?: return
        dao.deleteById(entryId)
    }
}
