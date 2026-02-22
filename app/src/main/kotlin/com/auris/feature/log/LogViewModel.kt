package com.auris.feature.log

import androidx.lifecycle.ViewModel
import com.auris.domain.model.MealType
import com.auris.domain.model.NutrientId
import com.auris.domain.model.ParsedFoodItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * UI state for the manual food entry form.
 */
data class LogUiState(
    val foodName: String  = "",
    val calories: String  = "",
    val protein: String   = "",
    val carbs: String     = "",
    val fat: String       = "",
    val mealType: MealType = MealType.LUNCH,
    val isSubmitting: Boolean = false
)

/**
 * LogViewModel
 * ────────────
 * Phase 4: manages manual log form + in-memory food log.
 *  • vitaminBoosts: cumulative nutrient boost map for Home screen animation.
 *  • Phase 5: injects FoodRepository and calls logFood() instead.
 */
@HiltViewModel
class LogViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LogUiState())
    val uiState: StateFlow<LogUiState> = _uiState.asStateFlow()

    private val _foodLog = MutableStateFlow<List<ParsedFoodItem>>(emptyList())
    val foodLog: StateFlow<List<ParsedFoodItem>> = _foodLog.asStateFlow()

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage.asStateFlow()

    /** Cumulative vitamin boosts from all logged items today */
    private val _vitaminBoosts = MutableStateFlow<Map<NutrientId, Float>>(emptyMap())
    val vitaminBoosts: StateFlow<Map<NutrientId, Float>> = _vitaminBoosts.asStateFlow()

    // ── Field setters ────────────────────────────────────────────────
    fun onFoodNameChanged(v: String)  = _uiState.update { it.copy(foodName = v) }
    fun onCaloriesChanged(v: String)  = _uiState.update { it.copy(calories = v) }
    fun onProteinChanged(v: String)   = _uiState.update { it.copy(protein  = v) }
    fun onCarbsChanged(v: String)     = _uiState.update { it.copy(carbs    = v) }
    fun onFatChanged(v: String)       = _uiState.update { it.copy(fat      = v) }
    fun onMealTypeChanged(v: MealType)= _uiState.update { it.copy(mealType = v) }

    // ── Submission ───────────────────────────────────────────────────
    fun submitFood() {
        val state = _uiState.value
        if (state.foodName.isBlank()) {
            _snackbarMessage.value = "⚠️  Please enter a food name"
            return
        }
        val item = ParsedFoodItem(
            name     = state.foodName.trim(),
            calories = state.calories.toFloatOrNull() ?: 0f,
            proteinG = state.protein.toFloatOrNull()  ?: 0f,
            carbsG   = state.carbs.toFloatOrNull()    ?: 0f,
            fatG     = state.fat.toFloatOrNull()      ?: 0f,
            mealType = state.mealType
        )

        // Update in-memory log
        _foodLog.update { it + item }

        // Accumulate vitamin boosts
        val boosts = item.estimateVitaminBoost()
        _vitaminBoosts.update { current ->
            val merged = current.toMutableMap()
            boosts.forEach { (k, v) -> merged[k] = (merged[k] ?: 0f) + v }
            merged
        }

        // Reset form (keep mealType)
        _uiState.update { LogUiState(mealType = it.mealType) }
        _snackbarMessage.value = "✅  ${item.name} logged!"
    }

    fun onSnackbarShown() {
        _snackbarMessage.value = ""
    }
}
