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
    /** True when HC is installed and we can attempt permission grant. */
    val isAvailable: Boolean = false,
    /** True when isAvailable AND all required permissions are granted. */
    val hasAllPermissions: Boolean = false,
    /**
     * True when HC is installed but an update is required before reading data.
     * The permission dialog can still be launched to pre-grant permissions.
     */
    val needsProviderUpdate: Boolean = false
)

/**
 * PermissionFlowViewModel — tracks Health Connect SDK status and permission state.
 *
 * Uses getSdkStatus() directly (HC 1.1.0) to distinguish:
 *   SDK_AVAILABLE                       → isAvailable=true
 *   SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED → isAvailable=true, needsProviderUpdate=true
 *   SDK_UNAVAILABLE                     → isAvailable=false
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
            val sdkStatus = kotlin.runCatching {
                HealthConnectClient.getSdkStatus(context)
            }.getOrDefault(HealthConnectClient.SDK_UNAVAILABLE)

            val isAvailable = sdkStatus == HealthConnectClient.SDK_AVAILABLE ||
                    sdkStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED

            val needsUpdate = sdkStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED

            val hasPerms = if (sdkStatus == HealthConnectClient.SDK_AVAILABLE) {
                kotlin.runCatching {
                    healthConnectRepository.hasAllPermissions()
                }.getOrDefault(false)
            } else false

            _state.value = HealthConnectPermissionState(
                isAvailable = isAvailable,
                hasAllPermissions = hasPerms,
                needsProviderUpdate = needsUpdate
            )
        }
    }
}
