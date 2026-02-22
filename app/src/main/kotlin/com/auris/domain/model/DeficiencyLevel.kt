package com.auris.domain.model

import androidx.compose.ui.graphics.Color
import com.auris.ui.theme.AurisColors

/**
 * DeficiencyLevel — 5-tier classification for nutrient intake.
 * Threshold fractions (effectiveIntake / adjustedRda):
 *   OPTIMAL   ≥ 0.80
 *   ADEQUATE  0.60 – 0.79
 *   LOW       0.40 – 0.59
 *   DEFICIENT 0.15 – 0.39
 *   CRITICAL  < 0.15
 */
enum class DeficiencyLevel(
    val label: String,
    val color: Color,
    val showBadge: Boolean
) {
    OPTIMAL   ("Optimal",   AurisColors.StatusOptimal,   false),
    ADEQUATE  ("Adequate",  AurisColors.StatusAdequate,  false),
    LOW       ("Low",       AurisColors.StatusLow,       true),
    DEFICIENT ("Deficient", AurisColors.StatusDeficient, true),
    CRITICAL  ("Critical",  AurisColors.StatusCritical,  true);

    companion object {
        fun of(fraction: Float): DeficiencyLevel = when {
            fraction >= 0.80f -> OPTIMAL
            fraction >= 0.60f -> ADEQUATE
            fraction >= 0.40f -> LOW
            fraction >= 0.15f -> DEFICIENT
            else              -> CRITICAL
        }
    }
}
