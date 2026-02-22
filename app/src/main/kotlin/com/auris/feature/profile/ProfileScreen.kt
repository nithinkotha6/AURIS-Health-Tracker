package com.auris.feature.profile

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.auris.ui.theme.AurisColors
import kotlinx.coroutines.flow.collectLatest

/**
 * ProfileScreen — Phase 10+ stub with Health Connect toggle.
 *
 * Shows:
 * - Basic header
 * - Health Connect sync toggle that reflects current permission state
 *   and allows the user to open the system permission flow.
 */
@Composable
fun ProfileScreen(
    viewModel: PermissionFlowViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val requiredPermissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class)
    )

    // PermissionController.createRequestPermissionResultContract() is the correct API for
    // Health Connect 1.0.0. HealthConnectClient.createRequestPermissionResultContract() was
    // only added in 1.1.x.
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = PermissionController.createRequestPermissionResultContract()
    ) { _: Set<String> ->
        // After the user returns from HC permission screen, refresh state.
        viewModel.refresh()
    }

    LaunchedEffect(Unit) {
        viewModel.refresh()
        profileViewModel.events.collectLatest { event ->
            snackbarHostState.showSnackbar(event)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AurisColors.BgPrimary)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Column {
                Text(
                    text = "Profile",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = AurisColors.LabelPrimary
                )
                Text(
                    text = "Configure data sources and privacy.",
                    fontSize = 14.sp,
                    color = AurisColors.LabelSecondary
                )
            }

            // Health Connect section
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .background(AurisColors.BgSecondary)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Health Connect",
                            style = MaterialTheme.typography.titleMedium,
                            color = AurisColors.LabelPrimary
                        )
                        Text(
                            text = if (state.isAvailable) {
                                if (state.hasAllPermissions) "Syncing steps, calories & sleep." else "Tap to grant access to steps, calories & sleep."
                            } else {
                                "Health Connect not available on this device."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = AurisColors.LabelSecondary
                        )
                    }
                    Switch(
                        checked = state.hasAllPermissions && state.isAvailable,
                        enabled = state.isAvailable,
                        onCheckedChange = { checked ->
                            if (checked && state.isAvailable) {
                                // Launch Health Connect's own permission UI
                                permissionLauncher.launch(requiredPermissions)
                            } else {
                                viewModel.refresh()
                            }
                        }
                    )
                }

                if (!state.isAvailable) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = AurisColors.Red
                        )
                        Text(
                            text = "Install Health Connect to sync Fitbit and burn-adjusted RDAs.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AurisColors.LabelTertiary,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            // Open Health Connect on Play Store to install / open
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("market://details?id=com.google.android.apps.healthdata")
                                setPackage("com.android.vending")
                            }
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata"))
                                )
                            }
                        },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text("Get Health Connect")
                    }
                }
            }

            // Phase 12: Export report
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .background(AurisColors.BgSecondary)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Export report",
                    style = MaterialTheme.typography.titleMedium,
                    color = AurisColors.LabelPrimary
                )
                Text(
                    text = "Generate a PDF summary of your nutrition and habits.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AurisColors.LabelSecondary
                )
                OutlinedButton(onClick = { profileViewModel.exportReport() }) {
                    Text("Export PDF")
                }
            }

            // Phase 12: Backup & Restore
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .background(AurisColors.BgSecondary)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Backup & restore",
                    style = MaterialTheme.typography.titleMedium,
                    color = AurisColors.LabelPrimary
                )
                Text(
                    text = "Save or restore your data as a local file.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AurisColors.LabelSecondary
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { profileViewModel.backupStub() }) {
                        Text("Backup")
                    }
                    OutlinedButton(onClick = { profileViewModel.restoreStub() }) {
                        Text("Restore")
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
