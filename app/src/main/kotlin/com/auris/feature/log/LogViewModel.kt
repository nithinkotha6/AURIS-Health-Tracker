package com.auris.feature.log

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auris.domain.model.MealType
import com.auris.domain.model.ParsedFoodItem
import com.auris.domain.repository.FoodRepository
import com.auris.domain.usecase.LogFoodUseCase
import com.auris.domain.usecase.ParseVoiceInputUseCase
import com.auris.domain.usecase.BuildPromptUseCase
import com.auris.domain.usecase.ParseDeepLinkUseCase
import com.auris.domain.usecase.ParseSharedTextUseCase
import com.auris.feature.log.ai.AiAppInstallDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.io.File
import androidx.core.content.FileProvider
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

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
 * Voice input state machine.
 */
enum class VoiceState {
    IDLE, LISTENING, PROCESSING, CONFIRMING, ERROR
}

/**
 * Camera/AI input state machine.
 */
enum class CameraState {
    IDLE, PREVIEW, CAPTURED, SENDING, AWAITING_RESPONSE
}

/**
 * LogViewModel
 * ────────────
 * Phase 7: voice logging + confirmation flow.
 * Phase 8: camera + AI deep-link + shared text.
 */
@HiltViewModel
class LogViewModel @Inject constructor(
    private val logFoodUseCase: LogFoodUseCase,
    private val foodRepository: FoodRepository,
    private val parseVoiceInput: ParseVoiceInputUseCase,
    private val voiceLogService: VoiceLogService,
    private val aiAppDetector: AiAppInstallDetector,
    private val buildPrompt: BuildPromptUseCase,
    private val parseDeepLink: ParseDeepLinkUseCase,
    private val parseSharedText: ParseSharedTextUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogUiState())
    val uiState: StateFlow<LogUiState> = _uiState.asStateFlow()

    /** Food log from the shared repository — updates reactively. */
    val foodLog: StateFlow<List<ParsedFoodItem>> = foodRepository.getTodayFoodLog()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage.asStateFlow()

    // ── Voice state ──────────────────────────────────────────────────
    private val _voiceState = MutableStateFlow(VoiceState.IDLE)
    val voiceState: StateFlow<VoiceState> = _voiceState.asStateFlow()

    private val _partialText = MutableStateFlow("")
    val partialText: StateFlow<String> = _partialText.asStateFlow()

    // ── Camera state (Phase 8) ───────────────────────────────────────
    private val _cameraState = MutableStateFlow(CameraState.IDLE)
    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()

    private val _capturedImageUri = MutableStateFlow<Uri?>(null)
    val capturedImageUri: StateFlow<Uri?> = _capturedImageUri.asStateFlow()

    // ── Pending confirmation item ────────────────────────────────────
    private val _pendingItem = MutableStateFlow<ParsedFoodItem?>(null)
    val pendingItem: StateFlow<ParsedFoodItem?> = _pendingItem.asStateFlow()

    private val _pendingSource = MutableStateFlow("voice")
    val pendingSource: StateFlow<String> = _pendingSource.asStateFlow()

    private val _ambiguousOptions = MutableStateFlow<List<ParsedFoodItem>>(emptyList())
    val ambiguousOptions: StateFlow<List<ParsedFoodItem>> = _ambiguousOptions.asStateFlow()

    /** Whether speech recognition is available on this device */
    val isVoiceAvailable: Boolean get() = voiceLogService.isAvailable()

    // ── Field setters ────────────────────────────────────────────────
    fun onFoodNameChanged(v: String)  = _uiState.update { it.copy(foodName = v) }
    fun onCaloriesChanged(v: String)  = _uiState.update { it.copy(calories = v) }
    fun onProteinChanged(v: String)   = _uiState.update { it.copy(protein  = v) }
    fun onCarbsChanged(v: String)     = _uiState.update { it.copy(carbs    = v) }
    fun onFatChanged(v: String)       = _uiState.update { it.copy(fat      = v) }
    fun onMealTypeChanged(v: MealType)= _uiState.update { it.copy(mealType = v) }

    // ── Manual submission via repository ─────────────────────────────
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

        _uiState.update { it.copy(isSubmitting = true) }
        viewModelScope.launch {
            val result = logFoodUseCase(item)
            _uiState.update { it.copy(isSubmitting = false) }

            if (result.isSuccess) {
                _uiState.update { LogUiState(mealType = it.mealType) }
                _snackbarMessage.value = "✅  ${item.name} logged!"
            } else {
                _snackbarMessage.value = "❌  Failed to log: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    // ── Voice logging ────────────────────────────────────────────────
    fun startVoiceInput() {
        _voiceState.value = VoiceState.LISTENING
        _partialText.value = ""

        voiceLogService.startListening(
            onResult = { text -> processVoiceResult(text) },
            onPartial = { text -> _partialText.value = text },
            onError = { code ->
                _voiceState.value = VoiceState.ERROR
                _snackbarMessage.value = "🎤  ${voiceLogService.errorMessage(code)}"
                _voiceState.value = VoiceState.IDLE
            },
            onReady = { /* mic indicator already showing via LISTENING state */ }
        )
    }

    fun stopVoiceInput() {
        voiceLogService.stopListening()
        if (_voiceState.value == VoiceState.LISTENING) {
            _voiceState.value = VoiceState.IDLE
        }
    }

    private fun processVoiceResult(text: String) {
        _voiceState.value = VoiceState.PROCESSING
        _partialText.value = text

        when (val result = parseVoiceInput(text)) {
            is ParseVoiceInputUseCase.VoiceParseResult.Success -> {
                _pendingItem.value = result.item
                _pendingSource.value = "voice"
                _voiceState.value = VoiceState.CONFIRMING
            }
            is ParseVoiceInputUseCase.VoiceParseResult.Ambiguous -> {
                _ambiguousOptions.value = result.options
                // Show first option as pending, user can change
                _pendingItem.value = result.options.firstOrNull()
                _pendingSource.value = "voice"
                _voiceState.value = VoiceState.CONFIRMING
            }
            is ParseVoiceInputUseCase.VoiceParseResult.NotFound -> {
                _voiceState.value = VoiceState.IDLE
                _snackbarMessage.value = "🎤  Could not identify \"$text\" — try manual entry"
            }
        }
    }

    // ── Confirmation flow (shared by voice + camera) ─────────────────
    fun confirmPendingItem(item: ParsedFoodItem) {
        viewModelScope.launch {
            val result = logFoodUseCase(item)
            if (result.isSuccess) {
                _snackbarMessage.value = "✅  ${item.name} logged!"
            } else {
                _snackbarMessage.value = "❌  Failed: ${result.exceptionOrNull()?.message}"
            }
            cancelPending()
        }
    }

    fun selectAmbiguousOption(item: ParsedFoodItem) {
        _pendingItem.value = item
        _ambiguousOptions.value = emptyList()
    }

    fun cancelPending() {
        _pendingItem.value = null
        _ambiguousOptions.value = emptyList()
        _voiceState.value = VoiceState.IDLE
        _cameraState.value = CameraState.IDLE
        _capturedImageUri.value = null
    }

    // ── Camera (Phase 8 stubs) ──────────────────────────────────────
    fun openCamera() {
        _cameraState.value = CameraState.PREVIEW
    }

    fun onPhotoCaptured(file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "com.auris.fileprovider",
            file
        )
        _capturedImageUri.value = uri
        _cameraState.value = CameraState.CAPTURED
    }

    fun closeCamera() {
        _cameraState.value = CameraState.IDLE
        _capturedImageUri.value = null
    }

    /**
     * Send the captured photo to the best available AI app with the generated prompt.
     */
    fun sendToAi(context: android.content.Context) {
        val imageUri = _capturedImageUri.value ?: return
        val aiBatch = aiAppDetector.detect()

        if (!aiBatch.anyAvailable) {
            _snackbarMessage.value = "⚠️  No supported AI apps (Gemini/ChatGPT) found"
            return
        }

        val prompt = buildPrompt()
        val targetPackage = aiBatch.preferredPackage!!

        val intent = aiAppDetector.createShareIntent(imageUri, prompt, targetPackage)
        _cameraState.value = CameraState.SENDING

        try {
            context.startActivity(intent)
            _cameraState.value = CameraState.AWAITING_RESPONSE
            _snackbarMessage.value = "🚀 Photo sent to ${if (targetPackage.contains("bard")) "Gemini" else "ChatGPT"}"
        } catch (e: Exception) {
            _cameraState.value = CameraState.IDLE
            _snackbarMessage.value = "❌ Failed to open AI app: ${e.message}"
        }
    }

    // ── Deep link result handling (Phase 8) ──────────────────────────
    fun handleEncodedDeepLink(encodedUri: String) {
        val decoded = Uri.decode(encodedUri)
        when (val result = parseDeepLink(decoded)) {
            is ParseDeepLinkUseCase.ParseResult.Success -> {
                handleDeepLinkItems(result.items)
            }
            is ParseDeepLinkUseCase.ParseResult.Failure -> {
                _snackbarMessage.value = "❌ Deep link error: ${result.reason}"
            }
        }
    }

    fun handleEncodedSharedText(encodedText: String) {
        val decoded = Uri.decode(encodedText)
        handleSharedText(decoded)
    }

    private fun handleSharedText(text: String) {
        when (val result = parseSharedText(text)) {
            is ParseSharedTextUseCase.SharedTextResult.DeepLinkFound -> {
                handleDeepLinkItems(result.result.items)
            }
            is ParseSharedTextUseCase.SharedTextResult.NoLinkFound -> {
                // If it's just raw text, we could try manual parsing or just show it
                _snackbarMessage.value = "📝 Shared: ${text.take(30)}..."
            }
            is ParseSharedTextUseCase.SharedTextResult.ParseError -> {
                _snackbarMessage.value = "❌ Shared text parse error: ${result.reason}"
            }
        }
    }

    fun handleDeepLinkItems(items: List<ParsedFoodItem>) {
        if (items.isEmpty()) return
        _pendingItem.value = items.first()
        _pendingSource.value = "camera"
        _cameraState.value = CameraState.IDLE
        _voiceState.value = VoiceState.CONFIRMING
        // TODO: handle multi-item confirmation for multi-food deep links
    }

    fun onSnackbarShown() {
        _snackbarMessage.value = ""
    }
}
