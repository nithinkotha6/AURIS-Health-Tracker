package com.auris.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auris.domain.model.ParsedFoodItem
import com.auris.domain.model.VitaminStatus
import com.auris.domain.repository.FoodRepository
import com.auris.domain.repository.VitaminRepository
import com.auris.domain.usecase.PredictiveNudgeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * HomeViewModel
 * ─────────────
 * Phase 5: provides reactive data to the Home dashboard.
 * Injects both FoodRepository and VitaminRepository so the Home screen
 * displays real-time vitamin status and food log summaries.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val vitaminRepository: VitaminRepository,
    private val nudgeUseCase: PredictiveNudgeUseCase
) : ViewModel() {

    /** Top 4 nutrients by lowest percentFraction — shown in the quick vitamins section. */
    val topVitamins: StateFlow<List<VitaminStatus>> = vitaminRepository.getTodayVitaminStatus()
        .map { list -> list.sortedBy { it.percentFraction }.take(4) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** All vitamin statuses for the full list view. */
    val allVitamins: StateFlow<List<VitaminStatus>> = vitaminRepository.getTodayVitaminStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Today's food log for summary display. */
    val todayFoodLog: StateFlow<List<ParsedFoodItem>> = foodRepository.getTodayFoodLog()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Today's total calories from all logged meals. */
    val totalCalories: StateFlow<Int> = foodRepository.getTodayFoodLog()
        .map { items -> items.sumOf { it.calories.toInt() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    /** Today's total protein from all logged meals. */
    val totalProtein: StateFlow<Float> = foodRepository.getTodayFoodLog()
        .map { items -> items.map { it.proteinG }.sum() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0f)

    private val _nudge = kotlinx.coroutines.flow.MutableStateFlow<PredictiveNudgeUseCase.NudgeResult?>(null)
    val nudge: StateFlow<PredictiveNudgeUseCase.NudgeResult?> = _nudge.asStateFlow()

    init {
        viewModelScope.launch {
            _nudge.value = nudgeUseCase.calculateNudge()
        }
    }
}
