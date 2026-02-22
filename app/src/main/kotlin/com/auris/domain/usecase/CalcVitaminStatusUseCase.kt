package com.auris.domain.usecase

import com.auris.domain.model.DeficiencyLevel
import com.auris.domain.model.NutrientId
import com.auris.domain.model.ParsedFoodItem
import com.auris.domain.model.VitaminStatus
import javax.inject.Inject
import javax.inject.Singleton

/**
 * CalcVitaminStatusUseCase
 * ─────────────────────────
 * Converts a food log ([List<ParsedFoodItem>]) into a full snapshot of
 * [VitaminStatus] for every tracked [NutrientId].
 *
 * Uses [ParsedFoodItem.estimateVitaminBoost] as the nutrient-proxy engine.
 * Phase 9: replaced by a proper USDA nutrient-database lookup.
 */
@Singleton
class CalcVitaminStatusUseCase @Inject constructor() {

    /**
     * Compute the vitamin status for all nutrients from the given [foods].
     * Returns one [VitaminStatus] per [NutrientId], using male RDA as the baseline.
     */
    operator fun invoke(foods: List<ParsedFoodItem>): List<VitaminStatus> {
        // Accumulate intake across all logged items
        val totals = mutableMapOf<NutrientId, Float>()
        for (food in foods) {
            food.estimateVitaminBoost().forEach { (nutrientId, amount) ->
                totals[nutrientId] = (totals[nutrientId] ?: 0f) + amount
            }
        }

        // Build a VitaminStatus for every NutrientId
        return NutrientId.entries.map { nutrientId ->
            val rda      = nutrientId.rdaMale
            val intake   = totals[nutrientId] ?: 0f
            val fraction = if (rda > 0f) (intake / rda).coerceIn(0f, 1f) else 0f

            VitaminStatus(
                nutrientId      = nutrientId,
                rawIntake       = intake,
                effectiveIntake = intake,
                adjustedRda     = rda,
                deficiencyLevel = DeficiencyLevel.of(fraction),
                displayValue    = formatIntake(intake, nutrientId.unit),
                sourceHint      = ""
            )
        }
    }

    private fun formatIntake(value: Float, unit: String): String {
        return if (value == 0f) "0 $unit"
        else "%.1f %s".format(value, unit)
    }
}
