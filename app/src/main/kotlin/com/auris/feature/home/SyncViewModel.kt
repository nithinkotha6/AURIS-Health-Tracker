package com.auris.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auris.domain.usecase.ApplyBurnAdjustmentsUseCase
import com.auris.domain.usecase.SyncHealthConnectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * SyncViewModel — orchestrates Health Connect sync on app foreground.
 *
 * Phase 10:
 * - Triggered from Home screen when lifecycle enters RESUMED.
 * - Invokes [SyncHealthConnectUseCase], which:
 *   - Reads today's activity/sleep metrics from Health Connect.
 *   - Applies burn adjustments to the vitamin log via VitaminRepository.
 */
@HiltViewModel
class SyncViewModel @Inject constructor(
    private val syncHealthConnectUseCase: SyncHealthConnectUseCase
) : ViewModel() {

    private val _lastBurnData = MutableStateFlow<ApplyBurnAdjustmentsUseCase.BurnData?>(null)
    val lastBurnData: StateFlow<ApplyBurnAdjustmentsUseCase.BurnData?> = _lastBurnData.asStateFlow()

    /**
     * Should be called when the app (or Home screen) comes to foreground.
     * Safe to call multiple times; the underlying use case is idempotent.
     */
    fun onForeground() {
        viewModelScope.launch {
            val burn = syncHealthConnectUseCase()
            _lastBurnData.value = burn
        }
    }
}

