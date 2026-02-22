package com.auris.domain.model

/**
 * MealType — meal context for a logged food entry.
 */
enum class MealType(val displayName: String) {
    BREAKFAST ("Breakfast"),
    LUNCH     ("Lunch"),
    DINNER    ("Dinner"),
    SNACK     ("Snack"),
    OTHER     ("Other")
}

/**
 * ParsedFoodItem — a single manually (or AI-parsed) logged food entry.
 * Phase 4: in-memory only.
 * Phase 6: mapped to FoodEntryEntity for Room persistence.
 * Phase 8: populated from AI deep-link / shared text.
 */
data class ParsedFoodItem(
    val name: String,
    val calories: Float = 0f,
    val proteinG: Float = 0f,
    val carbsG: Float = 0f,
    val fatG: Float = 0f,
    val mealType: MealType = MealType.LUNCH,
    val requiresConfirmation: Boolean = false,
    val sourceNote: String = ""
) {
    /**
     * Rough macro → vitamin boost proxy used in Phase 4 to animate
     * vitamin card updates immediately after logging.
     * Phase 9 replaces this with CalcVitaminStatusUseCase + absorption modifiers.
     */
    fun estimateVitaminBoost(): Map<NutrientId, Float> {
        val boosts = mutableMapOf<NutrientId, Float>()

        // Protein → B12 and Iron proxy
        if (proteinG > 0) {
            boosts[NutrientId.VIT_B12] = (proteinG / 56f) * 2.4f   // ~1 RDA-unit per daily protein
            boosts[NutrientId.IRON]    = (proteinG / 56f) * 8f
            boosts[NutrientId.PROTEIN] = proteinG
        }
        // Calories → Vit C and B-vitamins proxy
        if (calories > 0) {
            boosts[NutrientId.VIT_C]  = (calories / 2000f) * 90f
            boosts[NutrientId.VIT_B1] = (calories / 2000f) * 1.2f
            boosts[NutrientId.VIT_B3] = (calories / 2000f) * 16f
        }
        // Fat → fat-soluble vitamins
        if (fatG > 20f) {
            boosts[NutrientId.VIT_A] = (fatG / 80f) * 900f
            boosts[NutrientId.VIT_D] = (fatG / 80f) * 15f
            boosts[NutrientId.VIT_E] = (fatG / 80f) * 15f
            boosts[NutrientId.VIT_K] = (fatG / 80f) * 120f
        }
        // Carbs → magnesium / folate proxy
        if (carbsG > 0) {
            boosts[NutrientId.MAGNESIUM] = (carbsG / 300f) * 420f
            boosts[NutrientId.VIT_B9]    = (carbsG / 300f) * 400f
        }
        return boosts
    }
}
