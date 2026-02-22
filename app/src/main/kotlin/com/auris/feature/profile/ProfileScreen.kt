package com.auris.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.auris.ui.theme.AurisColors

/** ProfileScreen — Phase 12 stub */
@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AurisColors.BgPrimary),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("👤", fontSize = 48.sp)
            Text("Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = AurisColors.LabelPrimary)
            Text("Coming in Phase 12", fontSize = 13.sp, color = AurisColors.LabelTertiary)
        }
    }
}
