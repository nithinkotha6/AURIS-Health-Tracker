package com.auris.feature.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.auris.domain.model.ParsedFoodItem
import com.auris.feature.log.components.ManualFoodForm
import com.auris.ui.components.GlassCard
import com.auris.ui.theme.AurisColors

/**
 * LogScreen — Phase 4 manual food entry (iOS 17 light mode).
 */
@Composable
fun LogScreen(viewModel: LogViewModel = hiltViewModel()) {
    val uiState  by viewModel.uiState.collectAsStateWithLifecycle()
    val foodLog  by viewModel.foodLog.collectAsStateWithLifecycle()
    val snackMsg by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(snackMsg) {
        if (snackMsg.isNotEmpty()) {
            snackbarHostState.showSnackbar(snackMsg, duration = SnackbarDuration.Short)
            viewModel.onSnackbarShown()
        }
    }

    Scaffold(
        containerColor = AurisColors.BgPrimary,
        snackbarHost   = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                start  = 16.dp,
                end    = 16.dp,
                top    = padding.calculateTopPadding() + 16.dp,
                bottom = padding.calculateBottomPadding() + 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(AurisColors.BgPrimary)
        ) {
            item {
                Text("Food Log", fontSize = 34.sp, fontWeight = FontWeight.Bold,
                    color = AurisColors.LabelPrimary, letterSpacing = (-0.5).sp)
                Text("What did you eat today?", fontSize = 15.sp,
                    color = AurisColors.LabelSecondary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp))
            }

            item {
                ManualFoodForm(
                    state             = uiState,
                    onFoodNameChanged = viewModel::onFoodNameChanged,
                    onCaloriesChanged = viewModel::onCaloriesChanged,
                    onProteinChanged  = viewModel::onProteinChanged,
                    onCarbsChanged    = viewModel::onCarbsChanged,
                    onFatChanged      = viewModel::onFatChanged,
                    onMealTypeChanged = viewModel::onMealTypeChanged,
                    onSubmit          = viewModel::submitFood
                )
            }

            if (foodLog.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(top = 24.dp),
                        contentAlignment = Alignment.Center) {
                        Text("No meals logged yet today", fontSize = 14.sp,
                            color = AurisColors.LabelTertiary)
                    }
                }
            } else {
                item {
                    Text(
                        "TODAY — ${foodLog.size} ${if (foodLog.size == 1) "entry" else "entries"}".uppercase(),
                        fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp, color = AurisColors.LabelSecondary
                    )
                }
                items(foodLog.reversed()) { entry -> FoodLogItemRow(entry) }
            }
        }
    }
}

@Composable
private fun FoodLogItemRow(item: ParsedFoodItem) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontSize = 14.sp, fontWeight = FontWeight.Medium,
                    color = AurisColors.LabelPrimary)
                Text(item.mealType.displayName, fontSize = 11.sp,
                    color = AurisColors.LabelSecondary)
            }
            Column(horizontalAlignment = Alignment.End) {
                if (item.calories > 0f)
                    Text("${item.calories.toInt()} kcal", fontSize = 13.sp,
                        fontWeight = FontWeight.Bold, color = AurisColors.Orange)
                if (item.proteinG > 0f)
                    Text("${item.proteinG.toInt()}g protein", fontSize = 11.sp,
                        color = AurisColors.LabelSecondary)
            }
        }
    }
}
