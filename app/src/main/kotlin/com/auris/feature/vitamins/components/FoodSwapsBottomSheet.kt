package com.auris.feature.vitamins.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.auris.ui.theme.AurisColors

/**
 * FoodSwapsBottomSheet — tap LOW/DEFICIENT vitamin bar for suggestions (Phase 12 stub).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSwapsBottomSheet(
    nutrientName: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Text(
            text = "Food swaps for $nutrientName (Phase 12)",
            color = AurisColors.LabelPrimary,
            modifier = modifier
        )
    }
}
