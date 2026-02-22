package com.auris.domain.usecase

import com.auris.domain.model.NutrientId
import com.auris.domain.model.VitaminStatus
import javax.inject.Inject

/**
 * ApplyBurnAdjustmentsUseCase — adjusts nutrient RDAs based on physical burn
 * (exercise, steps) or poor sleep.
 *
 * Phase 9: Placeholder implementation for Health Connect integration.
 * In Phase 10+, this will read live data from the HealthConnectRepository.
 */
class ApplyBurnAdjustmentsUseCase @Inject constructor() {

    data class BurnData(
        val activeCalories: Float = 0f,
        val steps: Int = 0,
        val sleepHours: Float = 8f
    )

    /**
     * @param status Today's current vitamin status
     * @param burn Optional burn data; if null, assumes average sedentary day
     * @return Updated status with adjusted RDAs
     */
    operator fun invoke(
        status: List<VitaminStatus>,
        burn: BurnData? = null
    ): List<VitaminStatus> {
        val data = burn ?: BurnData()

        val rdaMultiplier = when {
            data.activeCalories > 600f -> 1.20f // High activity: +20% nutrient need
            data.activeCalories > 300f -> 1.10f // Moderate activity: +10%
            else -> 1.00f
        }

        val sleepDeficitMultiplier = if (data.sleepHours < 6f) 1.15f else 1.00f

        return status.map { vs ->
            var adjustedRda = vs.adjustedRda * rdaMultiplier

            // B-vitamins are burned faster during stress/lack of sleep
            if (isBVitamin(vs.nutrientId) && sleepDeficitMultiplier > 1f) {
                adjustedRda *= sleepDeficitMultiplier
            }

            vs.copy(adjustedRda = adjustedRda)
        }
    }

    private fun isBVitamin(id: NutrientId): Boolean = when (id) {
        NutrientId.VIT_B1, NutrientId.VIT_B2, NutrientId.VIT_B3, 
        NutrientId.VIT_B5, NutrientId.VIT_B6, NutrientId.VIT_B7, 
        NutrientId.VIT_B9, NutrientId.VIT_B12 -> true
        else -> false
    }
}
