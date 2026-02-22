package com.auris.feature.log

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * VoiceLogService — SpeechRecognizer wrapper for voice food logging.
 *
 * Phase 7: on-device speech-to-text (no API cost).
 *
 * Usage:
 *   voiceLogService.startListening(
 *     onResult = { text -> viewModel.processVoiceResult(text) },
 *     onError  = { code -> viewModel.onVoiceError(code) }
 *   )
 */
@Singleton
class VoiceLogService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var recognizer: SpeechRecognizer? = null

    /** Check if device supports speech recognition */
    fun isAvailable(): Boolean =
        SpeechRecognizer.isRecognitionAvailable(context)

    /** Start listening for speech input */
    fun startListening(
        onResult: (String) -> Unit,
        onPartial: (String) -> Unit = {},
        onError: (Int) -> Unit = {},
        onReady: () -> Unit = {}
    ) {
        stopListening() // Clean up any previous session

        recognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) = onReady()

                override fun onPartialResults(partialResults: Bundle?) {
                    val partial = partialResults
                        ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        ?.firstOrNull()
                    partial?.let { onPartial(it) }
                }

                override fun onResults(results: Bundle?) {
                    val text = results
                        ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        ?.firstOrNull()
                    text?.let { onResult(it) }
                }

                override fun onError(error: Int) = onError(error)

                // Stubs for remaining listener methods
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            }
            startListening(intent)
        }
    }

    /** Stop listening and release resources */
    fun stopListening() {
        recognizer?.apply {
            stopListening()
            cancel()
            destroy()
        }
        recognizer = null
    }

    /** Translate SpeechRecognizer error codes to user-friendly messages */
    fun errorMessage(errorCode: Int): String = when (errorCode) {
        SpeechRecognizer.ERROR_AUDIO              -> "Audio recording error"
        SpeechRecognizer.ERROR_CLIENT              -> "Client error"
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission required"
        SpeechRecognizer.ERROR_NETWORK             -> "Network error"
        SpeechRecognizer.ERROR_NETWORK_TIMEOUT     -> "Network timeout"
        SpeechRecognizer.ERROR_NO_MATCH            -> "Could not understand speech"
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY     -> "Speech recognizer busy"
        SpeechRecognizer.ERROR_SERVER               -> "Server error"
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT      -> "No speech detected"
        else                                        -> "Recognition error"
    }
}
