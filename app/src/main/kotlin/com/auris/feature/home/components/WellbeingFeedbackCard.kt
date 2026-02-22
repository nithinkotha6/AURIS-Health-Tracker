package com.auris.feature.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.auris.ui.theme.AurisColors

/**
 * WellbeingFeedbackCard — "How's your energy today?" (Phase 12 stub).
 * Shows 😴 Low, 🙂 Okay, ⚡ Great buttons when implemented.
 */
@Composable
fun WellbeingFeedbackCard(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(16.dp)) {
        Text(
            text = "How's your energy today? (Phase 12)",
            color = AurisColors.LabelSecondary
        )
    }
}
