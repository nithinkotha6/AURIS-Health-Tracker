package com.auris.feature.home.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auris.domain.usecase.PredictiveNudgeUseCase
import com.auris.ui.theme.AurisColors

/**
 * PredictiveNudgeCard — Phase 9 core UI component.
 * Displays a trending risk nutrient and suggested action.
 */
@Composable
fun PredictiveNudgeCard(
    nudge: PredictiveNudgeUseCase.NudgeResult,
    modifier: Modifier = Modifier
) {
    val isCritical = nudge.predictionPercent < 30

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = if (isCritical) 
                            listOf(Color(0xFFFFEEF0), Color(0xFFFFD1D6)) 
                        else 
                            listOf(AurisColors.PastelBlue.copy(alpha = 0.3f), AurisColors.PastelPurple.copy(alpha = 0.2f))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isCritical) Icons.Default.Warning else Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = if (isCritical) AurisColors.Red else AurisColors.Purple,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (isCritical) "Critical Insight" else "Predictive Nudge",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCritical) AurisColors.Red else AurisColors.LabelPrimary,
                            letterSpacing = 0.5.sp
                        )
                    }
                    
                    Surface(
                        color = (if (isCritical) AurisColors.Red else AurisColors.Purple).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(99.dp)
                    ) {
                        Text(
                            text = "${nudge.predictionPercent}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCritical) AurisColors.Red else AurisColors.Purple,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = nudge.message,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = AurisColors.LabelPrimary,
                    lineHeight = 22.sp
                )

                Spacer(Modifier.height(16.dp))

                // Trend indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .background(Color.White.copy(alpha = 0.5f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(nudge.predictionPercent / 100f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(99.dp))
                            .background(if (isCritical) AurisColors.Red else AurisColors.Purple)
                    )
                }
            }
        }
    }
}
