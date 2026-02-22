package com.auris.feature.log.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auris.domain.model.MealType
import com.auris.feature.log.LogUiState
import com.auris.ui.theme.AurisColors

/**
 * ManualFoodForm — Phase 4 food entry form.
 * iOS 17 light-mode style: FillTertiary fields, Blue submit button.
 */
@Composable
fun ManualFoodForm(
    state: LogUiState,
    onFoodNameChanged: (String) -> Unit,
    onCaloriesChanged: (String) -> Unit,
    onProteinChanged:  (String) -> Unit,
    onCarbsChanged:    (String) -> Unit,
    onFatChanged:      (String) -> Unit,
    onMealTypeChanged: (MealType) -> Unit,
    onSubmit:          () -> Unit,
    modifier: Modifier = Modifier
) {
    var mealDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AurisColors.BgSecondary)
            .border(1.dp, AurisColors.Separator, RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Add Food", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
            color = AurisColors.LabelPrimary)

        AurisTextField(
            value         = state.foodName,
            onValueChange = onFoodNameChanged,
            label         = "Food name",
            modifier      = Modifier.fillMaxWidth()
        )

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AurisTextField(value = state.calories, onValueChange = onCaloriesChanged,
                label = "Calories (kcal)", modifier = Modifier.weight(1f), isNumeric = true)
            AurisTextField(value = state.protein, onValueChange = onProteinChanged,
                label = "Protein (g)", modifier = Modifier.weight(1f), isNumeric = true)
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AurisTextField(value = state.carbs, onValueChange = onCarbsChanged,
                label = "Carbs (g)", modifier = Modifier.weight(1f), isNumeric = true)
            AurisTextField(value = state.fat, onValueChange = onFatChanged,
                label = "Fat (g)", modifier = Modifier.weight(1f), isNumeric = true)
        }

        // Meal type dropdown
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(AurisColors.FillTertiary)
                    .clickable { mealDropdownExpanded = true }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(state.mealType.displayName, fontSize = 14.sp, color = AurisColors.LabelPrimary)
                Text("▾", fontSize = 12.sp, color = AurisColors.LabelTertiary)
            }
            DropdownMenu(
                expanded = mealDropdownExpanded,
                onDismissRequest = { mealDropdownExpanded = false }
            ) {
                MealType.values().forEach { type ->
                    DropdownMenuItem(
                        text    = { Text(type.displayName) },
                        onClick = { onMealTypeChanged(type); mealDropdownExpanded = false }
                    )
                }
            }
        }

        // Submit — #007AFF blue, white text
        Button(
            onClick  = onSubmit,
            enabled  = !state.isSubmitting,
            colors   = ButtonDefaults.buttonColors(
                containerColor = AurisColors.Blue,
                contentColor   = Color.White
            ),
            shape    = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("LOG FOOD", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
    }
}

@Composable
private fun AurisTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isNumeric: Boolean = false
) {
    OutlinedTextField(
        value           = value,
        onValueChange   = onValueChange,
        label           = { Text(label, fontSize = 12.sp, color = AurisColors.LabelTertiary) },
        keyboardOptions = if (isNumeric)
            KeyboardOptions(keyboardType = KeyboardType.Decimal) else KeyboardOptions.Default,
        singleLine      = true,
        colors          = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = AurisColors.Blue,
            unfocusedBorderColor = AurisColors.Separator,
            focusedTextColor     = AurisColors.LabelPrimary,
            unfocusedTextColor   = AurisColors.LabelPrimary,
            cursorColor          = AurisColors.Blue
        ),
        modifier = modifier
    )
}
