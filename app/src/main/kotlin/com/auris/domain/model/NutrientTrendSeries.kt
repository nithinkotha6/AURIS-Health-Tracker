package com.auris.domain.model

import androidx.compose.ui.graphics.Color

/**
 * NutrientTrendSeries — 7-day percentage history for one nutrient.
 * Used by Overview screen (Phase 11) for sparkline charts.
 */
data class NutrientTrendSeries(
    val nutrientId: NutrientId,
    val name: String,
    val color: Color,
    val deficiencyLevel: DeficiencyLevel,
    /** Oldest to newest: 7 values 0–100 (percent of RDA). */
    val percentByDay: List<Float>
)
