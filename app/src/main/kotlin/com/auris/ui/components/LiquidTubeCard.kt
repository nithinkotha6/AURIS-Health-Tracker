package com.auris.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auris.domain.model.VitaminStatus
import com.auris.ui.theme.AurisColors
import kotlin.math.sin

/**
 * LiquidTubeCard
 * ──────────────
 * Vertical tube card with an animated liquid fill and sine-wave surface.
 * Displayed in the vitamins grid view.
 */
@Composable
fun LiquidTubeCard(
    vitamin: VitaminStatus,
    delayMs: Int = 0,
    modifier: Modifier = Modifier
) {
    val animatedFill by animateFloatAsState(
        targetValue   = vitamin.percentFraction,
        animationSpec = tween(durationMillis = 900, delayMillis = delayMs, easing = FastOutSlowInEasing),
        label         = "tube_${vitamin.nutrientId.name}"
    )

    val shouldWave = vitamin.percentFraction < 0.80f
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue   = 0f,
        targetValue    = (2 * Math.PI).toFloat(),
        animationSpec  = infiniteRepeatable(tween(1_500, easing = LinearEasing)),
        label          = "wavePhase"
    )

    val fillColor   = vitamin.deficiencyLevel.color
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(fillColor.copy(alpha = 0.85f), fillColor.copy(alpha = 0.30f))
    )

    GlassCard(modifier = modifier.width(100.dp).height(160.dp)) {
        Column(
            modifier          = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text       = vitamin.nutrientId.shortName,
                fontSize   = 11.sp,
                fontWeight = FontWeight.Bold,
                color      = AurisColors.LabelPrimary
            )
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 4.dp)
            ) {
                drawTube(animatedFill, fillColor, gradientBrush, shouldWave, wavePhase)
            }
            Text(
                text     = "${(vitamin.percentFraction * 100).toInt()}%",
                fontSize = 11.sp,
                color    = fillColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun DrawScope.drawTube(
    fillFraction: Float,
    fillColor: androidx.compose.ui.graphics.Color,
    fillBrush: Brush,
    doWave: Boolean,
    wavePhase: Float
) {
    val w = size.width
    val h = size.height
    val fillTop = h * (1f - fillFraction)   // Y coordinate where liquid starts

    // Wave surface
    val waveAmpPx = if (doWave) 3.dp.toPx() else 0f

    val liquidPath = Path().apply {
        val yWave = fillTop + sin(wavePhase.toDouble()).toFloat() * waveAmpPx
        moveTo(0f, yWave)
        // Sine wave across width
        val steps = 32
        for (i in 0..steps) {
            val x   = w * (i / steps.toFloat())
            val ang = wavePhase + (2 * Math.PI * i / steps).toFloat()
            val y   = fillTop + sin(ang.toDouble()).toFloat() * waveAmpPx
            lineTo(x, y)
        }
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }

    clipRect {
        drawPath(path = liquidPath, brush = fillBrush)
        // Highlight rim
        drawLine(
            color       = fillColor.copy(alpha = 0.6f),
            start       = Offset(0f, fillTop),
            end         = Offset(w, fillTop),
            strokeWidth = 1.5.dp.toPx()
        )
    }

    // Tube border
    drawRect(
        color       = AurisColors.CardBorder,
        topLeft     = Offset.Zero,
        size        = size,
        style       = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
    )
}
