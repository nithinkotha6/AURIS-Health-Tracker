package com.auris.feature.profile

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auris.domain.repository.HealthConnectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
 * - isAvailable is derived from HealthConnectClient.getSdkStatus() directly,
 *   independent of whether permissions have been granted yet.
 * - hasAllPermissions reflects whether all required permissions have been granted.
 */
@HiltViewModel
class PermissionFlowViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val healthConnectRepository: HealthConnectRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HealthConnectPermissionState())
    val state: StateFlow<HealthConnectPermissionState> = _state.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            // Check SDK availability independently from permissions
            val sdkStatus = kotlin.runCatching {
                HealthConnectClient.getSdkStatus(context)
            }.getOrDefault(HealthConnectClient.SDK_UNAVAILABLE)

            val isAvailable = sdkStatus == HealthConnectClient.SDK_AVAILABLE

            val hasPerms = if (isAvailable) {
                kotlin.runCatching {
                    healthConnectRepository.hasAllPermissions()
                }.getOrDefault(false)
            } else false

            _state.value = HealthConnectPermissionState(
                isAvailable = isAvailable,
                hasAllPermissions = hasPerms
            )
        }
    }
}
