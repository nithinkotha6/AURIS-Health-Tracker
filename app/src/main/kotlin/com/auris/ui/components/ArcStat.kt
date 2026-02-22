package com.auris.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auris.ui.theme.AurisColors

/**
 * ArcStat — half-arc (180°) metric widget.
 *
 * Redesigned: larger 88×52dp canvas, 22sp bold score, 12sp status label,
 * semantic color (green ≥70, yellow ≥40, red <40).
 */
@Composable
fun ArcStat(
    label: String,
    value: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animatedPct by animateFloatAsState(
        targetValue   = value / 100f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label         = "arcPct_$label"
    )

    // Semantic color: green ≥ 70, yellow ≥ 40, red < 40
    val arcColor = when {
        value >= 70 -> AurisColors.Green
        value >= 40 -> Color(0xFFFFCC00)   // yellow
        else        -> AurisColors.Red
    }

    // Status text
    val statusText = when {
        value >= 70 -> "Good"
        value >= 40 -> "Fair"
        else        -> "Low"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(100.dp)
    ) {
        // Label above gauge
        Text(
            text       = label,
            fontSize   = 13.sp,
            fontWeight = FontWeight.Medium,
            color      = AurisColors.LabelSecondary,
            letterSpacing = (-0.1).sp
        )

        Spacer(Modifier.height(8.dp))

        // Arc gauge
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.size(width = 88.dp, height = 52.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeW = 10.dp.toPx()
                val stroke  = Stroke(width = strokeW, cap = StrokeCap.Round)
                val r       = (this.size.width - strokeW) / 2f
                val cx      = this.size.width / 2f
                val cy      = this.size.height

                fun arcRect() = Size(r * 2, r * 2)
                fun arcOffset() = Offset(cx - r, cy - r * 2)

                // Track
                drawArc(
                    color      = AurisColors.FillTertiary,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter  = false,
                    topLeft    = arcOffset(),
                    size       = arcRect(),
                    style      = stroke
                )
                // Fill
                drawArc(
                    color      = arcColor,
                    startAngle = 180f,
                    sweepAngle = 180f * animatedPct,
                    useCenter  = false,
                    topLeft    = arcOffset(),
                    size       = arcRect(),
                    style      = stroke
                )
            }

            // Score — bold, inside arc
            Text(
                text       = "$value",
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold,
                color      = AurisColors.LabelPrimary,
                letterSpacing = (-0.5).sp,
                modifier   = Modifier.padding(bottom = 2.dp)
            )
        }

        Spacer(Modifier.height(4.dp))

        // Status label
        Text(
            text       = statusText,
            fontSize   = 12.sp,
            fontWeight = FontWeight.Medium,
            color      = arcColor,
            letterSpacing = 0.1.sp
        )
    }
}
