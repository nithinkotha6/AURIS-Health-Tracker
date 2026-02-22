package com.auris.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auris.domain.repository.HealthConnectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HealthConnectPermissionState(
    val isAvailable: Boolean = false,
    val hasAllPermissions: Boolean = false
)

/**
 * PermissionFlowViewModel — tracks Health Connect availability and permission state.
 *
 * Phase 10:
 * - Used by Profile screen to show a simple toggle for Health Connect sync.
 * - Actual permission request is driven by the UI via ActivityResult APIs;
 *   this ViewModel only reflects current state.
 */
@HiltViewModel
class PermissionFlowViewModel @Inject constructor(
    private val healthConnectRepository: HealthConnectRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HealthConnectPermissionState())
    val state: StateFlow<HealthConnectPermissionState> = _state.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            val sdkAvailable = kotlin.runCatching {
                healthConnectRepository.hasAllPermissions() || healthConnectRepository.readTodayBurnData() != null
            }.getOrDefault(false)

            val hasPerms = kotlin.runCatching {
                healthConnectRepository.hasAllPermissions()
            }.getOrDefault(false)

            _state.value = HealthConnectPermissionState(
                isAvailable = sdkAvailable,
                hasAllPermissions = hasPerms
            )
        }
    }
}

