package com.auris.domain.model

/**
 * VitaminStatus — nutrients status snapshot for a single day.
 */
data class VitaminStatus(
    val nutrientId: NutrientId,
    val rawIntake: Float = 0f,
    val effectiveIntake: Float = 0f,
    val adjustedRda: Float = 0f,
    val deficiencyLevel: DeficiencyLevel = DeficiencyLevel.OPTIMAL,
    val displayValue: String = "—",
    val sourceHint: String = ""
) {
    /** Fraction 0..1 of adjustedRda achieved; clamped for bar rendering. */
    val percentFraction: Float
        get() = if (adjustedRda <= 0f) 0f else (effectiveIntake / adjustedRda).coerceIn(0f, 1f)

    companion object {
        /**
         * Factory for test data: constructs a VitaminStatus from a simple
         * percentage complete value (0–100).
         */
        fun fromPercent(
            nutrientId: NutrientId,
            percentComplete: Int,
            displayValue: String = "${percentComplete}%",
            sourceHint: String = ""
        ): VitaminStatus {
            val fraction     = percentComplete / 100f
            val rdaMale      = nutrientId.rdaMale
            val effectiveInt = rdaMale * fraction
            return VitaminStatus(
                nutrientId      = nutrientId,
                rawIntake       = effectiveInt,
                effectiveIntake = effectiveInt,
                adjustedRda     = rdaMale,
                deficiencyLevel = DeficiencyLevel.of(fraction),
                displayValue    = displayValue,
                sourceHint      = sourceHint
            )
        }
    }
}
