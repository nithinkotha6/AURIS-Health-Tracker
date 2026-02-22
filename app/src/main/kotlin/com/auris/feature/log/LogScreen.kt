package com.auris.feature.log

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.auris.domain.model.ParsedFoodItem
import com.auris.feature.log.components.ConfirmationBottomSheet
import com.auris.feature.log.components.ManualFoodForm
import com.auris.ui.components.GlassCard
import com.auris.ui.theme.AurisColors

/**
 * LogScreen — Phase 7: manual + voice food entry (iOS 17 light mode).
 * Phase 8 adds camera FAB.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(
    encodedUri: String? = null,
    encodedSharedText: String? = null,
    viewModel: LogViewModel = hiltViewModel()
) {
    val uiState    by viewModel.uiState.collectAsStateWithLifecycle()
    val foodLog    by viewModel.foodLog.collectAsStateWithLifecycle()
    val snackMsg   by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val voiceState by viewModel.voiceState.collectAsStateWithLifecycle()
    val partialTxt by viewModel.partialText.collectAsStateWithLifecycle()
    val pendingItem by viewModel.pendingItem.collectAsStateWithLifecycle()
    val pendingSrc  by viewModel.pendingSource.collectAsStateWithLifecycle()
    val cameraState by viewModel.cameraState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    // Handle deep link / shared text trigger
    LaunchedEffect(encodedUri) {
        encodedUri?.let { viewModel.handleEncodedDeepLink(it) }
    }
    LaunchedEffect(encodedSharedText) {
        encodedSharedText?.let { viewModel.handleEncodedSharedText(it) }
    }
    LaunchedEffect(snackMsg) {
        if (snackMsg.isNotEmpty()) {
            snackbarHostState.showSnackbar(snackMsg, duration = SnackbarDuration.Short)
            viewModel.onSnackbarShown()
        }
    }

    Scaffold(
        containerColor = AurisColors.BgPrimary,
        snackbarHost   = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            // Dual FAB: Mic + Camera
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Camera FAB (Phase 8)
                SmallFloatingActionButton(
                    onClick = { viewModel.openCamera() },
                    containerColor = AurisColors.Green,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.CameraAlt, "Scan food")
                }

                // Voice FAB
                if (viewModel.isVoiceAvailable) {
                    val isListening = voiceState == VoiceState.LISTENING
                    val pulse = rememberInfiniteTransition(label = "pulse")
                    val pulseScale by pulse.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.15f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "micPulse"
                    )

                    FloatingActionButton(
                        onClick = {
                            if (isListening) viewModel.stopVoiceInput()
                            else viewModel.startVoiceInput()
                        },
                        containerColor = if (isListening) AurisColors.Orange else AurisColors.Blue,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = if (isListening) Modifier.scale(pulseScale) else Modifier
                    ) {
                        Icon(
                            imageVector = if (isListening) Icons.Filled.Stop else Icons.Filled.Mic,
                            contentDescription = if (isListening) "Stop listening" else "Voice log"
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = PaddingValues(
                    start  = 16.dp,
                    end    = 16.dp,
                    top    = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding() + 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(AurisColors.BgPrimary)
            ) {
                item {
                    Text("Food Log", fontSize = 34.sp, fontWeight = FontWeight.Bold,
                        color = AurisColors.LabelPrimary, letterSpacing = (-0.5).sp)
                    Text("What did you eat today?", fontSize = 15.sp,
                        color = AurisColors.LabelSecondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp))
                }

                // Voice listening indicator
                item {
                    AnimatedVisibility(
                        visible = voiceState == VoiceState.LISTENING || voiceState == VoiceState.PROCESSING,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Surface(
                            color = AurisColors.Blue.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (voiceState == VoiceState.LISTENING) "🎤 Listening..." else "Processing...",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AurisColors.Blue
                                )
                                if (partialTxt.isNotEmpty()) {
                                    Text(
                                        text = "\"$partialTxt\"",
                                        fontSize = 13.sp,
                                        color = AurisColors.LabelSecondary,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    ManualFoodForm(
                        state             = uiState,
                        onFoodNameChanged = viewModel::onFoodNameChanged,
                        onCaloriesChanged = viewModel::onCaloriesChanged,
                        onProteinChanged  = viewModel::onProteinChanged,
                        onCarbsChanged    = viewModel::onCarbsChanged,
                        onFatChanged      = viewModel::onFatChanged,
                        onMealTypeChanged = viewModel::onMealTypeChanged,
                        onSubmit          = viewModel::submitFood
                    )
                }

                if (foodLog.isEmpty()) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(top = 24.dp),
                            contentAlignment = Alignment.Center) {
                            Text("No meals logged yet today", fontSize = 14.sp,
                                color = AurisColors.LabelTertiary)
                        }
                    }
                } else {
                    item {
                        Text(
                            "TODAY — ${foodLog.size} ${if (foodLog.size == 1) "entry" else "entries"}".uppercase(),
                            fontSize = 11.sp, fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp, color = AurisColors.LabelSecondary
                        )
                    }
                    items(foodLog.reversed()) { entry -> FoodLogItemRow(entry) }
                }
            }

            // Camera Preview Overlay
            if (cameraState == CameraState.PREVIEW) {
                com.auris.feature.log.components.CameraPreview(
                    onPhotoCaptured = { file -> viewModel.onPhotoCaptured(file) },
                    onClose = { viewModel.closeCamera() }
                )
            }

            // Image Sharing Overlay (Processing captured photo)
            if (cameraState == CameraState.CAPTURED) {
                val context = androidx.compose.ui.platform.LocalContext.current
                LaunchedEffect(Unit) {
                    viewModel.sendToAi(context)
                }
            }

            // Confirmation Bottom Sheet (voice + camera AI)
            if (pendingItem != null) {
                ConfirmationBottomSheet(
                    item      = pendingItem!!,
                    source    = pendingSrc,
                    onConfirm = { viewModel.confirmPendingItem(it) },
                    onDismiss = { viewModel.cancelPending() }
                )
            }
        }
    }
}

@Composable
private fun FoodLogItemRow(item: ParsedFoodItem) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontSize = 14.sp, fontWeight = FontWeight.Medium,
                    color = AurisColors.LabelPrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(item.mealType.displayName, fontSize = 11.sp,
                        color = AurisColors.LabelSecondary)
                    if (item.sourceNote.isNotEmpty()) {
                        Text("• ${item.sourceNote}", fontSize = 11.sp,
                            color = AurisColors.LabelTertiary)
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                if (item.calories > 0f)
                    Text("${item.calories.toInt()} kcal", fontSize = 13.sp,
                        fontWeight = FontWeight.Bold, color = AurisColors.Orange)
                if (item.proteinG > 0f)
                    Text("${item.proteinG.toInt()}g protein", fontSize = 11.sp,
                        color = AurisColors.LabelSecondary)
            }
        }
    }
}
