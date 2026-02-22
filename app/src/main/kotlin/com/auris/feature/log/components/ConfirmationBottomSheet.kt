package com.auris.feature.log.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auris.domain.model.MealType
import com.auris.domain.model.ParsedFoodItem
import com.auris.ui.theme.AurisColors

/**
 * ConfirmationBottomSheet — pre-filled editable review form
 * for voice-parsed or AI-parsed food entries.
 *
 * Phase 7: Voice confirmation
 * Phase 8: AI food analysis confirmation with review flags
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationBottomSheet(
    item: ParsedFoodItem,
    source: String = "voice",
    onConfirm: (ParsedFoodItem) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var foodName by remember(item) { mutableStateOf(item.name) }
    var calories by remember(item) { mutableStateOf(item.calories.toInt().toString()) }
    var protein  by remember(item) { mutableStateOf(item.proteinG.let { if (it > 0) "%.1f".format(it) else "" }) }
    var carbs    by remember(item) { mutableStateOf(item.carbsG.let { if (it > 0) "%.1f".format(it) else "" }) }
    var fat      by remember(item) { mutableStateOf(item.fatG.let { if (it > 0) "%.1f".format(it) else "" }) }
    var mealType by remember(item) { mutableStateOf(item.mealType) }

    val isHighCal = (calories.toIntOrNull() ?: 0) > 2000

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AurisColors.BgSecondary,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (source == "voice") "🎤" else "📸",
                    fontSize = 20.sp
                )
                Text(
                    text = "Confirm Entry",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AurisColors.LabelPrimary
                )
                Spacer(Modifier.weight(1f))
                Surface(
                    color = if (source == "voice") AurisColors.Blue.copy(alpha = 0.15f)
                            else AurisColors.Green.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (source == "voice") "Voice" else "Camera AI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (source == "voice") AurisColors.Blue else AurisColors.Green,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // High-cal warning
            if (isHighCal) {
                Surface(
                    color = Color(0x20FF4444),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Warning, null,
                            tint = AurisColors.Orange, modifier = Modifier.size(18.dp))
                        Text("High calorie item — please verify",
                            fontSize = 13.sp, color = AurisColors.Orange,
                            fontWeight = FontWeight.Medium)
                    }
                }
            }

            // Editable fields
            OutlinedTextField(
                value = foodName,
                onValueChange = { foodName = it },
                label = { Text("Food Name") },
                singleLine = true,
                colors = confirmFieldColors(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("kcal") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = confirmFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it },
                    label = { Text("Protein (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = confirmFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = carbs,
                    onValueChange = { carbs = it },
                    label = { Text("Carbs (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = confirmFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it },
                    label = { Text("Fat (g)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = confirmFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )
            }

            // Meal type selector
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                MealType.entries.filter { it != MealType.OTHER }.forEach { type ->
                    FilterChip(
                        selected = mealType == type,
                        onClick = { mealType = type },
                        label = { Text(type.displayName, fontSize = 12.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AurisColors.Blue,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        val confirmed = ParsedFoodItem(
                            name     = foodName.trim(),
                            calories = calories.toFloatOrNull() ?: 0f,
                            proteinG = protein.toFloatOrNull()  ?: 0f,
                            carbsG   = carbs.toFloatOrNull()    ?: 0f,
                            fatG     = fat.toFloatOrNull()      ?: 0f,
                            mealType = mealType,
                            sourceNote = source
                        )
                        onConfirm(confirmed)
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AurisColors.Blue
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Confirm", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun confirmFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = AurisColors.Blue,
    unfocusedBorderColor = AurisColors.Separator,
    focusedLabelColor    = AurisColors.Blue,
    cursorColor          = AurisColors.Blue
)
