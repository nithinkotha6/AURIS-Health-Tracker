package com.auris.feature.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.auris.navigation.Screen
import com.auris.ui.components.GlassCard
import com.auris.ui.components.NutrientTrendChart
import com.auris.ui.theme.AurisColors
import java.time.format.DateTimeFormatter

/** OverviewScreen — 7-day nutrient trend charts + stats (Phase 11). */
@Composable
fun OverviewScreen(
    navController: NavController? = null,
    viewModel: OverviewViewModel = hiltViewModel()
) {
    val series by viewModel.trendSeries.collectAsStateWithLifecycle()
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val dateFormat = DateTimeFormatter.ofPattern("MMM d")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AurisColors.BgPrimary)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 120.dp)
    ) {
        Text(
            text = "Overview",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = AurisColors.LabelPrimary
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "7-day nutrient trends",
                fontSize = 14.sp,
                color = AurisColors.LabelSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
            if (navController != null) {
                Text(
                    text = "View all nutrients",
                    style = MaterialTheme.typography.labelMedium,
                    color = AurisColors.Blue,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable { navController.navigate(Screen.Vitamins.route) }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Stats summary
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Weekly avg (protein)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AurisColors.LabelSecondary
                    )
                    Text(
                        "${stats.weeklyAvgProteinPct.toInt()}%",
                        style = MaterialTheme.typography.titleMedium,
                        color = AurisColors.LabelPrimary
                    )
                }
                if (stats.bestDay != null || stats.worstDay != null) {
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        stats.bestDay?.let { day ->
                            Text(
                                "Best: ${day.format(dateFormat)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = AurisColors.LabelTertiary
                            )
                        }
                        stats.worstDay?.let { day ->
                            Text(
                                "Low: ${day.format(dateFormat)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = AurisColors.LabelTertiary
                            )
                        }
                    }
                }
                Text(
                    "${stats.daysWithData} days with data",
                    style = MaterialTheme.typography.labelSmall,
                    color = AurisColors.LabelTertiary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Trend charts per nutrient
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(
                items = series,
                key = { it.nutrientId.name }
            ) { s ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = s.name,
                                style = MaterialTheme.typography.titleSmall,
                                color = AurisColors.LabelPrimary
                            )
                            Text(
                                text = "${(s.percentByDay.lastOrNull() ?: 0f).toInt()}%",
                                style = MaterialTheme.typography.bodyMedium,
                                color = s.color
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        NutrientTrendChart(series = s)
                    }
                }
            }
        }
    }
}
