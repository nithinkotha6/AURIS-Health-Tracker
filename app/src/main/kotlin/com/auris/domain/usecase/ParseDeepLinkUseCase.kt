package com.auris.domain.usecase

import android.net.Uri
import com.auris.domain.model.MealType
import com.auris.domain.model.ParsedFoodItem
import javax.inject.Inject

/**
 * ParseDeepLinkUseCase — parses auris:// deep link URI into food items.
 *
 * URI format: auris://log?v=1&meal=lunch&items=GrilledChicken,310,32,0,7|WhiteRice,220,4,46,1
 *
 * Validates:
 * - Version (v=1)
 * - Meal type (breakfast|lunch|dinner|snack)
 * - Each item has exactly 5 fields
 * - Calories bounded 0-5000
 * - Macros bounded 0-1000
 * - Flags items >2000 kcal as requiresConfirmation
 */
class ParseDeepLinkUseCase @Inject constructor() {

    sealed class ParseResult {
        data class Success(
            val mealType: MealType,
            val items: List<ParsedFoodItem>
        ) : ParseResult()
        data class Failure(val reason: String) : ParseResult()
    }

    operator fun invoke(rawUri: String): ParseResult = try {
        val uri = Uri.parse(rawUri)

        // Validate scheme
        require(uri.scheme == "auris") { "Invalid scheme: ${uri.scheme}" }
        require(uri.host == "log") { "Invalid host: ${uri.host}" }

        // Validate version
        val version = uri.getQueryParameter("v")
        require(version == "1") { "Unsupported version: $version" }

        // Parse meal type
        val mealParam = uri.getQueryParameter("meal")?.lowercase()
        val mealType = when (mealParam) {
            "breakfast" -> MealType.BREAKFAST
            "lunch"     -> MealType.LUNCH
            "dinner"    -> MealType.DINNER
            "snack"     -> MealType.SNACK
            else        -> throw IllegalArgumentException("Invalid meal: $mealParam")
        }

        // Parse items
        val itemsRaw = uri.getQueryParameter("items")
            ?: throw IllegalArgumentException("Missing items parameter")
        require(itemsRaw.isNotBlank()) { "Empty items" }

        val items = itemsRaw.split("|").map { itemStr ->
            val parts = itemStr.split(",")
            require(parts.size == 5) { "Item must have 5 fields: $itemStr" }

            val name = parts[0].trim()
            require(name.isNotBlank()) { "Food name cannot be blank" }

            // Convert CamelCase to readable: "GrilledChicken" → "Grilled Chicken"
            val displayName = name.replace(Regex("([a-z])([A-Z])")) { mr ->
                "${mr.groupValues[1]} ${mr.groupValues[2]}"
            }

            val calories = parts[1].trim().toFloat()
            require(calories in 0f..5000f) { "Calories out of range: $calories" }

            val protein = parts[2].trim().toFloat()
            require(protein in 0f..1000f) { "Protein out of range: $protein" }

            val carbs = parts[3].trim().toFloat()
            require(carbs in 0f..1000f) { "Carbs out of range: $carbs" }

            val fat = parts[4].trim().toFloat()
            require(fat in 0f..1000f) { "Fat out of range: $fat" }

            ParsedFoodItem(
                name                 = displayName,
                calories             = calories,
                proteinG             = protein,
                carbsG               = carbs,
                fatG                 = fat,
                mealType             = mealType,
                requiresConfirmation = calories > 2000,
                sourceNote           = "camera"
            )
        }

        require(items.isNotEmpty()) { "No items parsed" }
        ParseResult.Success(mealType, items)

    } catch (e: Exception) {
        ParseResult.Failure(e.message ?: "Unknown parse error")
    }
}
