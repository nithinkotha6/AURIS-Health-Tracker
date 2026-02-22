package com.auris.feature.profile

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
 * ProfileScreen — Health Connect integration + export/backup.
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

    // PermissionController.createRequestPermissionResultContract() is the HC API
    // available in the compiled connect-client artifact for this project.
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = PermissionController.createRequestPermissionResultContract()
    ) { _: Set<String> ->
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

            // ── Health Connect section ──────────────────────────────────────
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AurisColors.BgSecondary, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                // Title row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        tint = AurisColors.Red,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Health Connect",
                        style = MaterialTheme.typography.titleMedium,
                        color = AurisColors.LabelPrimary
                    )
                    if (state.hasAllPermissions) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Connected",
                            tint = AurisColors.Green,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Status description
                Text(
                    text = when {
                        !state.isAvailable ->
                            "Health Connect is not installed on this device. Install it to sync Fitbit steps, calories & sleep."
                        state.needsProviderUpdate ->
                            "Health Connect needs an update. Tap below to grant permissions — you may be prompted to update first."
                        state.hasAllPermissions ->
                            "✓ Syncing steps, active calories & sleep from Health Connect."
                        else ->
                            "Tap below to grant AURIS access to your steps, active calories & sleep data."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = AurisColors.LabelSecondary
                )

                // Always-visible action button
                if (!state.isAvailable) {
                    // HC not installed: open Play Store
                    Button(
                        onClick = {
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
                        colors = ButtonDefaults.buttonColors(containerColor = AurisColors.Red),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Install Health Connect", color = Color.White)
                    }
                } else if (state.hasAllPermissions) {
                    // Already connected: offer to open HC settings to revoke
                    OutlinedButton(
                        onClick = {
                            try {
                                context.startActivity(
                                    context.packageManager
                                        .getLaunchIntentForPackage("com.google.android.apps.healthdata")
                                        ?: Intent(Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id=com.google.android.apps.healthdata"))
                                )
                            } catch (_: Exception) { }
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Manage in Health Connect")
                    }
                } else {
                    // HC installed but not permitted: launch HC permission dialog
                    Button(
                        onClick = { permissionLauncher.launch(requiredPermissions) },
                        colors = ButtonDefaults.buttonColors(containerColor = AurisColors.Blue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Grant Health Connect Access", color = Color.White)
                    }
                }
            }

            // ── Export report ───────────────────────────────────────────────
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AurisColors.BgSecondary, RoundedCornerShape(12.dp))
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
                OutlinedButton(
                    onClick = { profileViewModel.exportReport() },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Export PDF")
                }
            }

            // ── Backup & Restore ────────────────────────────────────────────
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AurisColors.BgSecondary, RoundedCornerShape(12.dp))
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
                    OutlinedButton(
                        onClick = { profileViewModel.backupStub() },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Backup")
                    }
                    OutlinedButton(
                        onClick = { profileViewModel.restoreStub() },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Restore")
                    }
                }
            }

            Spacer(Modifier.height(40.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
