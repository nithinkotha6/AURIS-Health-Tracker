package com.auris.domain.repository

import com.auris.domain.model.VitaminStatus
import com.auris.domain.usecase.ApplyBurnAdjustmentsUseCase
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * VitaminRepository — single source of truth for daily vitamin status.
 *
 * Phase 5: backed by in-memory fake (reacts to FoodRepository changes).
 * Phase 6: backed by Room DB (VitaminLogEntity).
 * Phase 10: optionally accepts Health Connect burn data when recalculating.
 */
interface VitaminRepository {

    /** Reactive stream of today's vitamin status for all 19 nutrients. */
    fun getTodayVitaminStatus(): Flow<List<VitaminStatus>>

    /**
     * Recalculate today's vitamin status from current food log.
     *
     * @param burn Optional burn data from Health Connect; when null,
     *             RDAs remain unmodified.
     */
    suspend fun recalculate(
        burn: ApplyBurnAdjustmentsUseCase.BurnData? = null
    )

    /** Reactive stream of vitamin status for a specific date. */
    fun getVitaminStatusByDate(date: LocalDate): Flow<List<VitaminStatus>>
}

