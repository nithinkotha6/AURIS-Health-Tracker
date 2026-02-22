package com.auris.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.auris.ui.theme.AurisColors

/**
 * RingWidget — iOS-style donut ring progress.
 * Animated with iOS spring easing. Center content slot for icon/text.
 */
@Composable
fun RingWidget(
    pct: Int,
    color: Color,
    size: Dp = 80.dp,
    strokeWidth: Dp = 10.dp,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val animatedPct by animateFloatAsState(
        targetValue   = pct / 100f,
        animationSpec = spring(
            dampingRatio  = Spring.DampingRatioLowBouncy,
            stiffness     = Spring.StiffnessMediumLow
        ),
        label = "ringPct"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke  = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            val padding = strokeWidth.toPx() / 2f
            val arcRect = Size(this.size.width - padding * 2, this.size.height - padding * 2)
            val topLeft = Offset(padding, padding)

            // Background track
            drawArc(
                color       = AurisColors.FillSecondary,
                startAngle  = -90f,
                sweepAngle  = 360f,
                useCenter   = false,
                topLeft     = topLeft,
                size        = arcRect,
                style       = stroke
            )
            // Filled arc
            drawArc(
                color       = color,
                startAngle  = -90f,
                sweepAngle  = 360f * animatedPct,
                useCenter   = false,
                topLeft     = topLeft,
                size        = arcRect,
                style       = stroke
            )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            content()
        }
    }
}
