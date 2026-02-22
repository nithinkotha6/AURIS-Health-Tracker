package com.auris.domain.usecase

import com.auris.domain.model.MealType
import java.time.LocalTime
import javax.inject.Inject

/**
 * BuildPromptUseCase — generates the AI prompt for food photo analysis.
 *
 * Phase 8: v1 prompt template with meal type inference.
 * The prompt instructs the AI to output an auris:// deep link URI.
 */
class BuildPromptUseCase @Inject constructor() {

    /**
     * @param mealTypeOverride Optional override; if null, inferred from time
     * @return The prompt text to send with the food photo
     */
    operator fun invoke(mealTypeOverride: MealType? = null): String {
        val mealType = mealTypeOverride ?: inferMealType()
        val mealApi = mealType.name.lowercase()

        return """
You are a precision nutrition analyst. Analyze the food in the attached image.

For EACH distinct food item visible, provide:
- Specific food name (CamelCase, no spaces)
- Calories (kcal, integer)
- Protein (g, one decimal max)
- Carbs (g, one decimal max)
- Fat (g, one decimal max)

After your explanation, output ONLY this line (no markdown, no extra spaces):
auris://log?v=1&meal=$mealApi&items=[name],[cal],[prot],[carb],[fat]|...

Rules:
- Use integers for calories
- One decimal maximum for macros (protein, carbs, fat)
- meal must be one of: breakfast, lunch, dinner, snack
- Separate multiple items with |
- Use CamelCase for food names (e.g., GrilledChicken, WhiteRice)
- If unsure about a food, make your best estimate
- Include all visible food items

Example output line:
auris://log?v=1&meal=$mealApi&items=GrilledChicken,310,32.0,0,7.0|WhiteRice,220,4.0,46.0,1.0
        """.trimIndent()
    }

    private fun inferMealType(): MealType {
        val hour = LocalTime.now().hour
        return when {
            hour in 5..10  -> MealType.BREAKFAST
            hour in 11..14 -> MealType.LUNCH
            hour in 15..16 -> MealType.SNACK
            hour in 17..21 -> MealType.DINNER
            else           -> MealType.SNACK
        }
    }
}
