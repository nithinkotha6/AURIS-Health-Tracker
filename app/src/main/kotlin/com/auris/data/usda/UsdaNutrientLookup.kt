package com.auris.data.usda

import com.auris.domain.model.MealType
import com.auris.domain.model.ParsedFoodItem
import javax.inject.Inject
import javax.inject.Singleton

/**
 * UsdaNutrientLookup — in-app static database of common foods → macros.
 *
 * Phase 7: provides instant offline macro lookup for voice-parsed food names.
 * Values sourced from USDA FoodData Central (per 1 serving / 100g default).
 */
@Singleton
class UsdaNutrientLookup @Inject constructor() {

    data class FoodEntry(
        val name: String,
        val servingDesc: String,
        val caloriesKcal: Float,
        val proteinG: Float,
        val carbsG: Float,
        val fatG: Float,
        val aliases: List<String> = emptyList()
    )

    private val database: List<FoodEntry> = listOf(
        // ── Fruits ───────────────────────────────────────────────────────
        FoodEntry("Apple",           "1 medium (182g)",    95f,  0.5f, 25f,   0.3f, listOf("apples")),
        FoodEntry("Banana",          "1 medium (118g)",   105f,  1.3f, 27f,   0.4f, listOf("bananas")),
        FoodEntry("Orange",          "1 medium (131g)",    62f,  1.2f, 15.4f, 0.2f, listOf("oranges")),
        FoodEntry("Strawberries",    "1 cup (152g)",       49f,  1f,   11.7f, 0.5f, listOf("strawberry")),
        FoodEntry("Blueberries",     "1 cup (148g)",       84f,  1.1f, 21.4f, 0.5f, listOf("blueberry")),
        FoodEntry("Grapes",          "1 cup (151g)",      104f,  1.1f, 27.3f, 0.2f, listOf("grape")),
        FoodEntry("Watermelon",      "1 cup diced (152g)", 46f,  0.9f, 11.5f, 0.2f),
        FoodEntry("Mango",           "1 cup (165g)",       99f,  1.4f, 24.7f, 0.6f, listOf("mangoes")),
        FoodEntry("Pineapple",       "1 cup (165g)",       82f,  0.9f, 21.6f, 0.2f),
        FoodEntry("Avocado",         "1 whole (201g)",    322f,  4f,   17.1f, 29.5f, listOf("avocados")),

        // ── Vegetables ───────────────────────────────────────────────────
        FoodEntry("Broccoli",        "1 cup chopped (91g)",31f,  2.6f,  6f,   0.3f),
        FoodEntry("Spinach",         "1 cup raw (30g)",     7f,  0.9f,  1.1f, 0.1f),
        FoodEntry("Carrot",          "1 medium (61g)",     25f,  0.6f,  5.8f, 0.1f, listOf("carrots")),
        FoodEntry("Sweet Potato",    "1 medium (114g)",   103f,  2.1f, 23.6f, 0.1f, listOf("sweet potatoes")),
        FoodEntry("Tomato",          "1 medium (123g)",    22f,  1.1f,  4.8f, 0.2f, listOf("tomatoes", "tomato")),
        FoodEntry("Cucumber",        "1 cup (104g)",       16f,  0.7f,  3.1f, 0.2f, listOf("cucumbers")),
        FoodEntry("Bell Pepper",     "1 medium (119g)",    31f,  1f,    6f,   0.3f, listOf("pepper", "peppers")),
        FoodEntry("Onion",           "1 medium (110g)",    44f,  1.2f, 10.3f, 0.1f, listOf("onions")),
        FoodEntry("Corn",            "1 ear (90g)",        77f,  2.9f, 17.1f, 1.1f),
        FoodEntry("Lettuce",         "1 cup shredded (47g)",5f,  0.5f,  1f,   0.1f),

        // ── Protein ──────────────────────────────────────────────────────
        FoodEntry("Chicken Breast",  "1 breast (172g)",   284f, 53.4f,  0f,   6.2f, listOf("chicken", "grilled chicken")),
        FoodEntry("Salmon",          "1 fillet (178g)",   367f, 39.3f,  0f,  22.1f, listOf("grilled salmon", "baked salmon")),
        FoodEntry("Tuna",            "1 can (165g)",      191f, 42.1f,  0f,   1.4f, listOf("canned tuna")),
        FoodEntry("Shrimp",          "3 oz (85g)",         84f, 20.4f,  0f,   0.2f, listOf("prawns")),
        FoodEntry("Ground Beef",     "3 oz (85g)",        213f, 22f,    0f,  13f,   listOf("beef", "hamburger", "burger patty")),
        FoodEntry("Steak",           "6 oz (170g)",       340f, 42f,    0f,  18f,   listOf("ribeye", "sirloin", "filet")),
        FoodEntry("Turkey Breast",   "3 oz (85g)",        125f, 26f,    0f,   1.8f, listOf("turkey")),
        FoodEntry("Pork Chop",       "1 chop (113g)",     187f, 30.2f,  0f,   6.9f, listOf("pork")),
        FoodEntry("Tofu",            "1/2 cup (126g)",     94f, 10f,    2.3f, 5.9f),
        FoodEntry("Tempeh",          "1 cup (166g)",      319f, 33.7f,  8.9f,18.2f),

        // ── Eggs & Dairy ─────────────────────────────────────────────────
        FoodEntry("Egg",             "1 large (50g)",      78f,  6.3f,  0.6f, 5.3f, listOf("eggs", "boiled egg", "fried egg", "scrambled egg")),
        FoodEntry("Greek Yogurt",    "1 cup (245g)",      100f, 17f,    6f,   0.7f, listOf("yogurt", "yoghurt")),
        FoodEntry("Milk",            "1 cup (244ml)",     149f,  8f,   12f,   8f,   listOf("whole milk")),
        FoodEntry("Cheese",          "1 oz (28g)",        113f,  7f,    0.4f, 9f,   listOf("cheddar", "mozzarella")),
        FoodEntry("Cottage Cheese",  "1 cup (226g)",      206f, 28f,    6.2f, 9f),
        FoodEntry("Butter",          "1 tbsp (14g)",      102f,  0.1f,  0f,  11.5f),

        // ── Grains & Legumes ─────────────────────────────────────────────
        FoodEntry("White Rice",      "1 cup cooked (186g)",206f, 4.3f, 44.5f, 0.4f, listOf("rice")),
        FoodEntry("Brown Rice",      "1 cup cooked (195g)",216f, 5f,   44.8f, 1.8f),
        FoodEntry("Pasta",           "1 cup cooked (140g)",220f, 8.1f, 43.2f, 1.3f, listOf("spaghetti", "noodles", "penne")),
        FoodEntry("Bread",           "1 slice (30g)",      79f,  3.6f, 13.1f, 1f,   listOf("toast", "white bread", "wheat bread")),
        FoodEntry("Oatmeal",         "1 cup cooked (234g)",166f, 5.9f, 28.1f, 3.6f, listOf("oats", "porridge")),
        FoodEntry("Quinoa",          "1 cup cooked (185g)",222f, 8.1f, 39.4f, 3.6f),
        FoodEntry("Lentils",         "1 cup cooked (198g)",230f,17.9f, 39.9f, 0.8f, listOf("dal", "daal")),
        FoodEntry("Black Beans",     "1 cup cooked (172g)",227f,15.2f, 40.8f, 0.9f, listOf("beans")),
        FoodEntry("Chickpeas",       "1 cup cooked (164g)",269f,14.5f, 45f,   4.2f, listOf("garbanzo")),
        FoodEntry("Peanut Butter",   "2 tbsp (32g)",      190f,  7f,    7f,  16f,   listOf("pb")),

        // ── Nuts & Seeds ─────────────────────────────────────────────────
        FoodEntry("Almonds",         "1 oz (28g)",        164f,  6f,    6.1f,14.2f, listOf("almond")),
        FoodEntry("Walnuts",         "1 oz (28g)",        185f,  4.3f,  3.9f,18.5f, listOf("walnut")),
        FoodEntry("Cashews",         "1 oz (28g)",        157f,  5.2f,  8.6f,12.4f, listOf("cashew")),
        FoodEntry("Chia Seeds",      "1 oz (28g)",        138f,  4.7f, 12.3f, 8.7f, listOf("chia")),
        FoodEntry("Flaxseed",        "1 tbsp (10g)",       55f,  1.9f,  3f,   4.3f, listOf("flax")),

        // ── Beverages ────────────────────────────────────────────────────
        FoodEntry("Orange Juice",    "1 cup (248ml)",     112f,  1.7f, 25.8f, 0.5f, listOf("oj")),
        FoodEntry("Coffee",          "1 cup (237ml)",       2f,  0.3f,  0f,   0f,   listOf("black coffee")),
        FoodEntry("Green Tea",       "1 cup (245ml)",       2f,  0.5f,  0f,   0f,   listOf("tea")),
        FoodEntry("Protein Shake",   "1 scoop (30g)",     120f, 24f,    3f,   1f,   listOf("whey", "protein powder")),
        FoodEntry("Smoothie",        "1 cup (240ml)",     150f,  3f,   30f,   2f,   listOf("fruit smoothie")),

        // ── Common Meals ─────────────────────────────────────────────────
        FoodEntry("Pizza",           "1 slice (107g)",    285f, 12.2f, 35.6f,10.4f, listOf("pizza slice")),
        FoodEntry("Hamburger",       "1 burger (226g)",   540f, 34f,   40f,  27f,   listOf("cheeseburger")),
        FoodEntry("Hot Dog",         "1 frank (52g)",     151f,  5.3f,  1.8f,13.5f),
        FoodEntry("French Fries",    "medium (117g)",     365f,  4.4f, 44.4f,19.4f, listOf("fries")),
        FoodEntry("Burrito",         "1 burrito (340g)",  500f, 22f,   58f,  18f),
        FoodEntry("Sushi Roll",      "6 pieces (180g)",   255f,  9f,   38f,   7f,   listOf("sushi", "california roll")),
        FoodEntry("Caesar Salad",    "1 bowl (300g)",     360f, 22f,   14f,  24f,   listOf("salad")),
        FoodEntry("Sandwich",        "1 whole (250g)",    400f, 20f,   40f,  16f,   listOf("sub", "club sandwich")),
        FoodEntry("Soup",            "1 bowl (245g)",     150f, 10f,   15f,   5f,   listOf("chicken soup", "tomato soup")),
        FoodEntry("Tacos",           "2 tacos (170g)",    340f, 16f,   30f,  16f,   listOf("taco")),
        FoodEntry("Fried Rice",      "1 cup (198g)",      238f,  5.5f, 35f,   9f),
        FoodEntry("Pancakes",        "2 pancakes (152g)", 340f,  8f,   44f,  14f,   listOf("pancake")),
        FoodEntry("Waffles",         "1 waffle (75g)",    218f,  5.9f, 24.7f,10.6f, listOf("waffle")),

        // ── Snacks & Sweets ──────────────────────────────────────────────
        FoodEntry("Granola Bar",     "1 bar (40g)",       190f,  3f,   29f,   7f,   listOf("protein bar", "energy bar")),
        FoodEntry("Dark Chocolate",  "1 oz (28g)",        155f,  1.4f, 17.4f, 9f,   listOf("chocolate")),
        FoodEntry("Ice Cream",       "1/2 cup (66g)",     137f,  2.3f, 15.6f, 7.3f),
        FoodEntry("Cookie",          "1 medium (30g)",    140f,  1.5f, 19f,   7f,   listOf("cookies")),
        FoodEntry("Chips",           "1 oz (28g)",        152f,  2f,   15f,  10f,   listOf("potato chips", "crisps")),
        FoodEntry("Popcorn",         "3 cups (24g)",       93f,  3f,   18.6f, 1.1f),
        FoodEntry("Trail Mix",       "1/4 cup (40g)",     173f,  5f,   16f,  11f),
        FoodEntry("Hummus",          "2 tbsp (30g)",       70f,  2f,    4f,   5f),
        FoodEntry("Rice Cake",       "1 cake (9g)",        35f,  0.7f,  7.3f, 0.3f, listOf("rice cakes")),
    )

    /** All known food names for display/search */
    val allFoodNames: List<String> get() = database.map { it.name }

    /**
     * Find best matching food entry for the given query.
     * Returns null if no reasonable match found.
     */
    fun findBestMatch(query: String): FoodEntry? {
        val normalized = query.trim().lowercase()
        if (normalized.isBlank()) return null

        // Exact name match
        database.firstOrNull { it.name.lowercase() == normalized }?.let { return it }

        // Exact alias match
        database.firstOrNull { entry ->
            entry.aliases.any { it.lowercase() == normalized }
        }?.let { return it }

        // Contains match (food name within query or query within food name)
        database.firstOrNull { entry ->
            normalized.contains(entry.name.lowercase()) ||
            entry.name.lowercase().contains(normalized) ||
            entry.aliases.any { alias ->
                normalized.contains(alias.lowercase()) || alias.lowercase().contains(normalized)
            }
        }?.let { return it }

        return null
    }

    /**
     * Find all foods matching the query (for ambiguous results).
     */
    fun findAllMatches(query: String, limit: Int = 5): List<FoodEntry> {
        val normalized = query.trim().lowercase()
        if (normalized.isBlank()) return emptyList()

        return database.filter { entry ->
            normalized.contains(entry.name.lowercase()) ||
            entry.name.lowercase().contains(normalized) ||
            entry.aliases.any { alias ->
                normalized.contains(alias.lowercase()) || alias.lowercase().contains(normalized)
            }
        }.take(limit)
    }

    /**
     * Scale a food entry by a quantity multiplier.
     */
    fun scale(entry: FoodEntry, quantity: Float, mealType: MealType = MealType.LUNCH): ParsedFoodItem {
        return ParsedFoodItem(
            name     = if (quantity != 1f) "${entry.name} × ${quantity.let { if (it % 1 == 0f) it.toInt().toString() else "%.1f".format(it) }}" else entry.name,
            calories = entry.caloriesKcal * quantity,
            proteinG = entry.proteinG * quantity,
            carbsG   = entry.carbsG * quantity,
            fatG     = entry.fatG * quantity,
            mealType = mealType,
            sourceNote = "voice"
        )
    }
}
