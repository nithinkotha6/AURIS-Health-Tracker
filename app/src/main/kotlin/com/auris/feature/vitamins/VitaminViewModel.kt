package com.auris.feature.vitamins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auris.domain.model.VitaminStatus
import com.auris.domain.repository.VitaminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * VitaminViewModel
 * ─────────────────
 * Phase 5: injects VitaminRepository and collects real reactive flows.
 * Replaced hardcoded test data with repository-sourced data that
 * updates whenever the food log changes.
 */
@HiltViewModel
class VitaminViewModel @Inject constructor(
    vitaminRepository: VitaminRepository
) : ViewModel() {

    /** All 19 nutrients — updates reactively when food is logged. */
    val vitamins: StateFlow<List<VitaminStatus>> = vitaminRepository.getTodayVitaminStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Top 4 nutrients by lowest percentFraction — used on Home screen. */
    val topFour: StateFlow<List<VitaminStatus>> = vitaminRepository.getTodayVitaminStatus()
        .map { list -> list.sortedBy { it.percentFraction }.take(4) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
