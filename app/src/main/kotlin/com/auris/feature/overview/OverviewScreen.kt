package com.auris.feature.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.auris.ui.theme.AurisColors

/** OverviewScreen — Phase 11 stub (used as deep-link placeholder in Phase 8) */
@Composable
fun OverviewScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AurisColors.BgPrimary),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("📊", fontSize = 48.sp)
            Text("Overview", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = AurisColors.LabelPrimary)
            Text("Trend charts in Phase 11", fontSize = 13.sp, color = AurisColors.LabelTertiary)
        }
    }
}
