package com.auris.domain.usecase

import com.auris.data.usda.UsdaNutrientLookup
import com.auris.domain.model.MealType
import com.auris.domain.model.ParsedFoodItem
import java.time.LocalTime
import javax.inject.Inject

/**
 * ParseVoiceInputUseCase — extracts food + quantity from spoken phrases.
 *
 * Flow: raw speech text → regex quantity extraction → USDA lookup → result
 *
 * Examples:
 *   "I ate two eggs"         → Egg × 2, 156 kcal
 *   "half an orange"         → Orange × 0.5, 31 kcal
 *   "a cup of rice"          → White Rice × 1, 206 kcal
 *   "chicken breast"         → Chicken Breast × 1, 284 kcal
 *   "something weird"        → NotFound
 */
class ParseVoiceInputUseCase @Inject constructor(
    private val usdaLookup: UsdaNutrientLookup
) {

    sealed class VoiceParseResult {
        data class Success(val item: ParsedFoodItem) : VoiceParseResult()
        data class Ambiguous(val options: List<ParsedFoodItem>) : VoiceParseResult()
        data class NotFound(val rawText: String) : VoiceParseResult()
    }

    /** Quantity words → numeric multiplier */
    private val quantityWords = mapOf(
        "half"    to 0.5f,
        "quarter" to 0.25f,
        "one"     to 1f,
        "a"       to 1f,
        "an"      to 1f,
        "two"     to 2f,
        "three"   to 3f,
        "four"    to 4f,
        "five"    to 5f,
        "six"     to 6f,
        "seven"   to 7f,
        "eight"   to 8f,
        "nine"    to 9f,
        "ten"     to 10f
    )

    /** Filler words to strip from the input */
    private val fillerWords = setOf(
        "i", "ate", "had", "just", "of", "some", "the", "about",
        "around", "like", "cup", "cups", "piece", "pieces", "slice",
        "slices", "bowl", "plate", "serving", "servings", "glass",
        "handful", "with", "and", "for", "my"
    )

    operator fun invoke(rawText: String): VoiceParseResult {
        if (rawText.isBlank()) return VoiceParseResult.NotFound(rawText)

        val words = rawText.trim().lowercase().split("\\s+".toRegex())

        // Extract quantity
        var quantity = 1f
        val foodWords = mutableListOf<String>()

        for (word in words) {
            when {
                quantityWords.containsKey(word) -> quantity = quantityWords[word]!!
                word.toFloatOrNull() != null     -> quantity = word.toFloat()
                !fillerWords.contains(word)      -> foodWords.add(word)
            }
        }

        val foodQuery = foodWords.joinToString(" ")
        if (foodQuery.isBlank()) return VoiceParseResult.NotFound(rawText)

        // Determine meal type from current time
        val mealType = inferMealType()

        // Try exact match first
        val bestMatch = usdaLookup.findBestMatch(foodQuery)
        if (bestMatch != null) {
            val item = usdaLookup.scale(bestMatch, quantity, mealType)
            return VoiceParseResult.Success(item)
        }

        // Try individual words if multi-word query failed
        if (foodWords.size > 1) {
            for (word in foodWords) {
                val match = usdaLookup.findBestMatch(word)
                if (match != null) {
                    val item = usdaLookup.scale(match, quantity, mealType)
                    return VoiceParseResult.Success(item)
                }
            }
        }

        // Find all partial matches for ambiguous result
        val allMatches = usdaLookup.findAllMatches(foodQuery)
        if (allMatches.isNotEmpty()) {
            val items = allMatches.map { usdaLookup.scale(it, quantity, mealType) }
            return VoiceParseResult.Ambiguous(items)
        }

        return VoiceParseResult.NotFound(rawText)
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
