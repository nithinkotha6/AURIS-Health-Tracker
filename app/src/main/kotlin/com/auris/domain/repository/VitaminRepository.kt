package com.auris.domain.repository

import com.auris.domain.model.VitaminStatus
import com.auris.domain.usecase.ApplyBurnAdjustmentsUseCase
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * VitaminRepository — provides reactive streams of computed vitamin status.
 *
 * Phase 5: implemented by [FakeVitaminRepositoryImpl] (in-memory).
 * Phase 6: implemented by [RoomVitaminRepositoryImpl] (Room/SQLite).
 */
interface VitaminRepository {

    /** Flow of today's vitamin status, recalculated whenever the food log changes. */
    fun getTodayVitaminStatus(): Flow<List<VitaminStatus>>

    /**
     * Force a recalculation with an optional burn adjustment.
     * @param burn optional workout / cardio burn data to apply absorption modifiers.
     */
    suspend fun recalculate(burn: ApplyBurnAdjustmentsUseCase.BurnData? = null)

    /** Historical vitamin status for a specific [date]. */
    fun getVitaminStatusByDate(date: LocalDate): Flow<List<VitaminStatus>>
}
