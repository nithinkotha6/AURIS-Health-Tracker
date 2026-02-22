package com.auris.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.auris.domain.model.NutrientTrendSeries

/**
 * NutrientTrendChart — 7-day sparkline for one nutrient (Phase 11).
 * Uses Canvas; line color matches [series].color (deficiency level).
 */
@Composable
fun NutrientTrendChart(
    series: NutrientTrendSeries,
    modifier: Modifier = Modifier
) {
    val points = series.percentByDay
    val visible by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(600),
        label = "chart_visibility"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        if (points.size < 2) return@Canvas
        val maxPct = points.maxOrNull()?.coerceAtLeast(1f) ?: 100f
        val strokeWidth = 2.dp.toPx()
        val padding = strokeWidth * 2
        val w = size.width - padding * 2
        val h = size.height - padding * 2

        val path = Path().apply {
            val stepX = w / (points.size - 1).coerceAtLeast(1)
            points.forEachIndexed { i, pct ->
                val x = padding + i * stepX
                val y = padding + h - (pct / maxPct * h * visible).toFloat()
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
        }
        drawPath(
            path = path,
            color = series.color,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}
