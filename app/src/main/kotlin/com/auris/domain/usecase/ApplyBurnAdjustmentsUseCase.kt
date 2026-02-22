package com.auris.domain.usecase

import com.auris.domain.model.VitaminStatus
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ApplyBurnAdjustmentsUseCase
 * ────────────────────────────
 * Phase 9: applies workout / cardio burn data as absorption modifiers on top
 *          of a baseline list of [VitaminStatus] entries.
 *
 * Currently a pass-through stub — full implementation in Phase 9.
 */
@Singleton
class ApplyBurnAdjustmentsUseCase @Inject constructor() {

    /**
     * Burn data supplied by the Health Connect / workout tracker integration.
     *
     * @param cardioKcal  calories burned through cardio exercise.
     * @param strengthMin minutes of strength training.
     */
    data class BurnData(
        val cardioKcal: Float = 0f,
        val strengthMin: Float = 0f
    )

    /**
     * Apply [burn] modifiers to [statuses].
     * Phase 9 will implement real absorption-factor math here.
     */
    operator fun invoke(
        statuses: List<VitaminStatus>,
        burn: BurnData?
    ): List<VitaminStatus> = statuses // pass-through until Phase 9
}
