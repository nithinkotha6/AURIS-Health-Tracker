package com.auris.data.repository

import com.auris.domain.model.ParsedFoodItem
import com.auris.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * FakeFoodRepositoryImpl — in-memory implementation of [FoodRepository].
 *
 * Phase 5: used as the singleton data source shared across all ViewModels.
 * Phase 6: replaced by [RoomFoodRepositoryImpl].
 *
 * Scoped as @Singleton so logged meals persist across navigation during the session.
 */
@Singleton
class FakeFoodRepositoryImpl @Inject constructor() : FoodRepository {

    /** In-memory store: maps date → list of food entries  */
    private val _store = MutableStateFlow<Map<LocalDate, List<FoodEntry>>>(emptyMap())

    override suspend fun logFood(item: ParsedFoodItem) {
        val today = LocalDate.now()
        val entry = FoodEntry(
            id       = UUID.randomUUID().toString(),
            date     = today,
            item     = item
        )
        _store.update { current ->
            val todayEntries = current[today].orEmpty()
            current + (today to (todayEntries + entry))
        }
    }

    override fun getTodayFoodLog(): Flow<List<ParsedFoodItem>> {
        val today = LocalDate.now()
        return _store.map { store ->
            store[today]?.map { it.item }.orEmpty()
        }
    }

    override fun getFoodLogByDate(date: LocalDate): Flow<List<ParsedFoodItem>> {
        return _store.map { store ->
            store[date]?.map { it.item }.orEmpty()
        }
    }

    override suspend fun deleteFood(id: String) {
        _store.update { current ->
            current.mapValues { (_, entries) ->
                entries.filter { it.id != id }
            }
        }
    }

    /** Internal wrapper to add an ID and date to each food item. */
    private data class FoodEntry(
        val id: String,
        val date: LocalDate,
        val item: ParsedFoodItem
    )
}
