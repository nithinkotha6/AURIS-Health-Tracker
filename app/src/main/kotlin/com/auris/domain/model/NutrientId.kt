package com.auris.domain.model

/**
 * NutrientId — enum of all 19 nutrients tracked by AURIS.
 * Holds display name, SI unit, and sex-specific RDA values.
 */
enum class NutrientId(
    val displayName: String,
    val shortName: String,
    val unit: String,
    val rdaMale: Float,
    val rdaFemale: Float,
    val upperLimit: Float? = null
) {
    VIT_A     ("Vitamin A",   "Vit A",  "mcg RAE",  900f,  700f, 3000f),
    VIT_B1    ("Thiamine",    "B1",     "mg",        1.2f,  1.1f,    null),
    VIT_B2    ("Riboflavin",  "B2",     "mg",        1.3f,  1.1f,    null),
    VIT_B3    ("Niacin",      "B3",     "mg NE",    16f,   14f,   35f),
    VIT_B5    ("Pantothenic", "B5",     "mg",        5f,    5f,     null),
    VIT_B6    ("Pyridoxine",  "B6",     "mg",        1.3f,  1.3f,  100f),
    VIT_B7    ("Biotin",      "B7",     "mcg",      30f,   30f,    null),
    VIT_B9    ("Folate",      "B9",     "mcg DFE", 400f,  400f,  1000f),
    VIT_B12   ("Cobalamin",   "B12",    "mcg",       2.4f,  2.4f,   null),
    VIT_C     ("Vitamin C",   "Vit C",  "mg",       90f,   75f,  2000f),
    VIT_D     ("Vitamin D",   "Vit D",  "mcg",      15f,   15f,   100f),
    VIT_E     ("Vitamin E",   "Vit E",  "mg AT",    15f,   15f,  1000f),
    VIT_K     ("Vitamin K",   "Vit K",  "mcg",     120f,   90f,    null),
    IRON      ("Iron",        "Fe",     "mg",        8f,   18f,    45f),
    CALCIUM   ("Calcium",     "Ca",     "mg",     1000f, 1000f,  2500f),
    MAGNESIUM ("Magnesium",   "Mg",     "mg",      420f,  320f,   350f),
    ZINC      ("Zinc",        "Zn",     "mg",       11f,    8f,    40f),
    PROTEIN   ("Protein",     "Prot",   "g",        56f,   46f,    null),
    COLLAGEN  ("Collagen",    "Coll",   "mg",     2500f, 2500f,    null);

    fun rda(isMale: Boolean) = if (isMale) rdaMale else rdaFemale
}
