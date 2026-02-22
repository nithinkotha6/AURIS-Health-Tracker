package com.auris.domain.usecase

import com.auris.domain.model.NutrientId
import com.auris.domain.repository.VitaminRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

/**
 * PredictiveNudgeUseCase — analyzes 3-day history to identify lowest
 * trending nutrients and suggest corrective action.
 */
class PredictiveNudgeUseCase @Inject constructor(
    private val vitaminRepository: VitaminRepository
) {

    data class NudgeResult(
        val highestRisk: NutrientId,
        val message: String,
        val predictionPercent: Int
    )

    /**
     * Analyzes trends and returns a nudge for the nutrient in greatest deficit.
     */
    suspend fun calculateNudge(): NudgeResult? {
        val today = LocalDate.now()
        val history = mutableListOf<Map<NutrientId, Float>>()

        // Get 3 days of status percentages
        for (i in 0..2) {
            val date = today.minusDays(i.toLong())
            val status = vitaminRepository.getVitaminStatusByDate(date).first()
            val map = status.associate { it.nutrientId to it.percentFraction }
            if (map.isNotEmpty()) history.add(map)
        }

        if (history.isEmpty()) return null

        // Calculate average and trend (slope) for each nutrient
        val nutrientStats = NutrientId.entries.map { nid ->
            val percents = history.map { it[nid] ?: 0f }
            val avg = percents.average().toFloat()
            // simple trend: change from 2 days ago to today
            val trend = if (percents.size >= 3) percents[0] - percents[2] else 0f
            nid to (avg - (trend * 0.5f)) // score: lower avg + downward trend = higher risk
        }.sortedBy { it.second }

        val worst = nutrientStats.first()
        val nutrientId = worst.first
        val score = worst.second

        return NudgeResult(
            highestRisk = nutrientId,
            message = generateMessage(nutrientId, score),
            predictionPercent = (score * 100).toInt().coerceIn(0, 100)
        )
    }

    private fun generateMessage(id: NutrientId, score: Float): String = when {
        score < 0.2f -> "Critical: ${id.displayName} levels are very low. Focus on ${getFoodSource(id)} today."
        score < 0.5f -> "${id.displayName} is trending low (${(score*100).toInt()}%). Consider adding ${getFoodSource(id)} to your next meal."
        else -> "You're doing great! Keep it up with a side of ${getFoodSource(id)} for optimal ${id.displayName}."
    }

    private fun getFoodSource(id: NutrientId): String = when (id) {
        NutrientId.VIT_A -> "carrots or spinach"
        NutrientId.VIT_B1 -> "pork or sunflower seeds"
        NutrientId.VIT_B2 -> "eggs or almonds"
        NutrientId.VIT_B3 -> "chicken or tuna"
        NutrientId.VIT_B5 -> "mushrooms or avocado"
        NutrientId.VIT_B6 -> "bananas or chickpeas"
        NutrientId.VIT_B7 -> "eggs or sweet potatoes"
        NutrientId.VIT_B9 -> "lentils or leafy greens"
        NutrientId.VIT_B12 -> "beef or nutritional yeast"
        NutrientId.VIT_C -> "citrus fruits or peppers"
        NutrientId.VIT_D -> "fatty fish or sun exposure"
        NutrientId.VIT_E -> "almonds or sunflower oil"
        NutrientId.VIT_K -> "kale or broccoli"
        NutrientId.IRON -> "red meat or spinach"
        NutrientId.CALCIUM -> "yogurt or sardines"
        NutrientId.MAGNESIUM -> "pumpkin seeds or dark chocolate"
        NutrientId.ZINC -> "oysters or beef"
        NutrientId.PROTEIN -> "chicken or Greek yogurt"
        NutrientId.COLLAGEN -> "bone broth or chicken skin"
    }
}
