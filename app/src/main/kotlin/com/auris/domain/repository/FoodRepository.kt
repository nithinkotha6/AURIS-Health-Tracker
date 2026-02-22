package com.auris.domain.repository

import com.auris.domain.model.ParsedFoodItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * FoodRepository — provides reactive access to the food log.
 *
 * Phase 4: implemented in-memory inside [LogViewModel].
 * Phase 5: extracted here and consumed by [FakeVitaminRepositoryImpl].
 * Phase 6: implemented by [RoomFoodRepositoryImpl] backed by Room.
 */
interface FoodRepository {

    /** Flow of all food entries logged for today. */
    fun getTodayFoodLog(): Flow<List<ParsedFoodItem>>

    /** Flow of all food entries logged for a specific [date]. */
    fun getFoodLogByDate(date: LocalDate): Flow<List<ParsedFoodItem>>

    /** Persist a new [item] to today's log. */
    suspend fun logFood(item: ParsedFoodItem)
}
