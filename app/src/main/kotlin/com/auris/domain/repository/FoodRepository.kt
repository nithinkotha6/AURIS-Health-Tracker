package com.auris.domain.repository

import com.auris.domain.model.ParsedFoodItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * FoodRepository — single source of truth for food logging.
 *
 * Phase 5: backed by in-memory fake.
 * Phase 6: backed by Room DB.
 */
interface FoodRepository {

    /** Persist a food item to today's log. */
    suspend fun logFood(item: ParsedFoodItem)

    /** Reactive stream of today's food entries. */
    fun getTodayFoodLog(): Flow<List<ParsedFoodItem>>

    /** Reactive stream of food entries for a specific date. */
    fun getFoodLogByDate(date: LocalDate): Flow<List<ParsedFoodItem>>

    /** Remove a specific food entry by its id. */
    suspend fun deleteFood(id: String)
}
