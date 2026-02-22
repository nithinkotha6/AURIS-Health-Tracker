package com.auris.domain.usecase

import com.auris.domain.model.DeficiencyLevel
import com.auris.domain.model.NutrientId
import com.auris.domain.model.ParsedFoodItem
import com.auris.domain.model.VitaminStatus
import javax.inject.Inject

/**
 * CalcVitaminStatusUseCase — computes vitamin status for all 19 nutrients
 * from a list of food entries.
 *
 * Phase 5 (v1): uses ParsedFoodItem.estimateVitaminBoost() as a macro→vitamin proxy.
 * Phase 9: will add absorption modifiers and burn adjustments.
 */
class CalcVitaminStatusUseCase @Inject constructor() {

    /**
     * @param foods Today's food entries
     * @param isMale Gender for RDA selection (defaults to male)
     * @return Complete list of VitaminStatus for all 19 nutrients
     */
    operator fun invoke(
        foods: List<ParsedFoodItem>,
        isMale: Boolean = true
    ): List<VitaminStatus> {
        val rawBoosts = mutableMapOf<NutrientId, Float>()
        val effectiveBoosts = mutableMapOf<NutrientId, Float>()

        // 1. Calculate raw and meal-based effective boosts
        foods.forEach { food ->
            val foodBoosts = food.estimateVitaminBoost()
            
            // Raw accumulation
            foodBoosts.forEach { (nid, amt) -> 
                rawBoosts[nid] = (rawBoosts[nid] ?: 0f) + amt
            }

            // Apply meal-based modifiers
            foodBoosts.forEach { (nid, amt) ->
                var effectiveAmt = amt

                when (nid) {
                    NutrientId.VIT_A -> {
                        if (food.fatG > 5f) effectiveAmt *= 1.20f // +20% for fat-soluble Vit A
                    }
                    NutrientId.IRON -> {
                        val hasVitC = foodBoosts.containsKey(NutrientId.VIT_C) || 
                                     foods.any { it.name.contains("juice", true) || it.name.contains("orange", true) }
                        if (hasVitC) effectiveAmt *= 1.25f // +25% Iron with Vit C
                    }
                    NutrientId.CALCIUM -> {
                        if (foodBoosts.containsKey(NutrientId.VIT_D)) effectiveAmt *= 1.10f // +10% Calc with Vit D
                    }
                    NutrientId.ZINC -> {
                        val isHighPhytate = food.carbsG > 40f && 
                            (food.name.contains("bean", true) || food.name.contains("grain", true) || food.name.contains("rice", true))
                        if (isHighPhytate) effectiveAmt *= 0.70f // -30% Zinc from phytates
                    }
                    else -> {}
                }
                effectiveBoosts[nid] = (effectiveBoosts[nid] ?: 0f) + effectiveAmt
            }
        }

        // 2. Apply day-based modifiers (e.g., Vit D + Magnesium synergy)
        if (effectiveBoosts.containsKey(NutrientId.VIT_D) && effectiveBoosts.containsKey(NutrientId.MAGNESIUM)) {
            effectiveBoosts[NutrientId.VIT_D] = effectiveBoosts[NutrientId.VIT_D]!! * 1.15f
        }

        // 3. Build VitaminStatus for every tracked nutrient
        return NutrientId.entries.map { nutrientId ->
            val rda = nutrientId.rda(isMale)
            val rawIntake = rawBoosts[nutrientId] ?: 0f
            val effectiveIntake = effectiveBoosts[nutrientId] ?: 0f
            
            val fraction = if (rda > 0f) (effectiveIntake / rda).coerceIn(0f, 1f) else 0f

            VitaminStatus(
                nutrientId      = nutrientId,
                rawIntake       = rawIntake,
                effectiveIntake = effectiveIntake,
                adjustedRda     = rda,
                deficiencyLevel = DeficiencyLevel.of(fraction),
                displayValue    = formatDisplayValue(effectiveIntake, nutrientId.unit),
                sourceHint      = if (foods.isEmpty()) "" else "${foods.size} item(s)"
            )
        }
    }

    private fun formatDisplayValue(amount: Float, unit: String): String {
        return when {
            amount >= 100f  -> "${amount.toInt()} $unit"
            amount >= 1f    -> "${"%.1f".format(amount)} $unit"
            amount > 0f     -> "${"%.2f".format(amount)} $unit"
            else            -> "0 $unit"
        }
    }
}
