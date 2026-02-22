package com.auris.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auris.ui.theme.AurisColors

/**
 * LiquidColumn — animated vertical liquid fill tube.
 * Synced to JSX: w-[52px] h-[250px] bg-white/40 backdrop-blur-md
 * rounded-[26px], border-[0.5px] border-white/60, p-1 inner.
 */
@Composable
fun LiquidColumn(
    label: String,
    value: String,
    unit: String,
    pct: Int,
    gradTop: Color,
    gradBot: Color,
    accentColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    var mounted by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { mounted = true }

    val animatedPct by animateFloatAsState(
        targetValue   = if (mounted) pct / 100f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label         = "liquid_$label"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier.width(64.dp)   // w-[64px] container in JSX
    ) {
        // Value + unit label above tube
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                color = AurisColors.LabelPrimary, letterSpacing = (-0.2).sp)
            Text(unit, fontSize = 13.sp, fontWeight = FontWeight.Medium,
                color = AurisColors.LabelSecondary)
        }

        // Tube — 52dp wide, 250dp tall, glass look
        Box(
            modifier = Modifier
                .width(52.dp)              // w-[52px]
                .height(250.dp)            // h-[250px]
                .shadow(2.dp, RoundedCornerShape(26.dp), ambientColor = Color(0x0D000000))
                .clip(RoundedCornerShape(26.dp))     // rounded-[26px]
                .background(Color(0x66FFFFFF))       // bg-white/40
                .border(0.5.dp, Color(0x99FFFFFF), RoundedCornerShape(26.dp))  // border-white/60
                .padding(4.dp)             // p-1 = 4dp
        ) {
            // Liquid fill
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(animatedPct)
                    .clip(RoundedCornerShape(22.dp))   // rounded-[22px] inner
                    .background(Brush.verticalGradient(listOf(gradTop, gradBot)))
            ) {
                // Specular highlight stripe (right side, 30% width)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 4.dp)
                        .width(8.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0x33FFFFFF))   // bg-white/20
                )
                // Icon inside liquid at bottom
                Icon(
                    imageVector        = icon,
                    contentDescription = label,
                    tint               = Color.White.copy(alpha = 0.88f),
                    modifier           = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .size(20.dp)
                )
            }
        }

        // Percentage label below tube
        Text("$pct%", fontSize = 16.sp, fontWeight = FontWeight.Bold,
            color = AurisColors.LabelPrimary)
    }
}
