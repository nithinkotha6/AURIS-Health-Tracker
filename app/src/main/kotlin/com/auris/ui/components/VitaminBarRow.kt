package com.auris.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auris.ui.theme.AurisColors

/**
 * VitaminBarRow — iOS grouped list row (HIG StatsView / VitaminRow).
 *
 * Layout:
 *   [36dp icon square] [name + LOW badge] [% + amount unit]
 *   [4dp progress bar — full width, semantic color]
 *
 * Semantic color from pct:
 *   ≥80% green · ≥60% blue · ≥40% orange · <40% red
 */
@Composable
fun VitaminBarRow(
    name: String,
    icon: ImageVector,
    iconColor: Color,
    percent: Int,
    amount: String,
    unit: String,
    isLow: Boolean = false,
    isLast: Boolean = false,
    modifier: Modifier = Modifier
) {
    val pctColor = AurisColors.pctColor(percent)

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val animPct by animateFloatAsState(
        targetValue   = if (visible) percent / 100f else 0f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label         = "bar_$name"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 13.dp)
            .then(
                if (!isLast) Modifier.padding(bottom = 0.dp) else Modifier
            )
    ) {
        // Top row: icon · name · badge · pct + amount
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Colored icon square (36×36, 9dp corner)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(iconColor.copy(alpha = 0.09f))
            ) {
                Icon(icon, contentDescription = name,
                    tint = iconColor, modifier = Modifier.size(17.dp))
            }

            // Name + LOW badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                    color = AurisColors.LabelPrimary)
                if (isLow) {
                    Text(
                        text       = "LOW",
                        fontSize   = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color      = AurisColors.Red,
                        modifier   = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AurisColors.Red.copy(alpha = 0.10f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Percent + amount
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("$percent%", fontSize = 14.sp, fontWeight = FontWeight.Bold,
                        color = pctColor)
                    Spacer(Modifier.width(5.dp))
                    Text("$amount $unit", fontSize = 12.sp, color = AurisColors.LabelTertiary)
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        // 4dp progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(AurisColors.FillTertiary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animPct)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(999.dp))
                    .background(pctColor)
            )
        }

        // Hairline separator (except last row)
        if (!isLast) {
            Spacer(Modifier.height(0.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(Color(0x173C3C43))
                    .padding(top = 13.dp)
            )
        }
    }
}
