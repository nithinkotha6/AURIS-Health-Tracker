package com.auris.feature.log.ai

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AiAppInstallDetector — detects which AI apps are installed on the device.
 *
 * Phase 8: routes intent to the best available AI app.
 *
 * Detection priority:
 * 1. Google Gemini (com.google.android.apps.bard)
 * 2. OpenAI ChatGPT (com.openai.chatgpt)
 */
@Singleton
class AiAppInstallDetector @Inject constructor(
    @ApplicationContext private val context: Context
) {

    data class AiAvailability(
        val geminiInstalled: Boolean = false,
        val chatgptInstalled: Boolean = false
    ) {
        val anyAvailable: Boolean get() = geminiInstalled || chatgptInstalled

        /** Preferred package name for intent routing */
        val preferredPackage: String? get() = when {
            geminiInstalled  -> GEMINI_PACKAGE
            chatgptInstalled -> CHATGPT_PACKAGE
            else             -> null
        }
    }

    companion object {
        const val GEMINI_PACKAGE  = "com.google.android.apps.bard"
        const val CHATGPT_PACKAGE = "com.openai.chatgpt"
    }

    /**
     * Check which AI apps are available on the device.
     */
    fun detect(): AiAvailability {
        val pm = context.packageManager
        return AiAvailability(
            geminiInstalled  = isInstalled(pm, GEMINI_PACKAGE),
            chatgptInstalled = isInstalled(pm, CHATGPT_PACKAGE)
        )
    }

    /**
     * Create an ACTION_SEND intent with image + prompt text, targeted
     * at the specified AI app package.
     */
    fun createShareIntent(
        imageUri: android.net.Uri,
        promptText: String,
        targetPackage: String
    ): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            putExtra(Intent.EXTRA_TEXT, promptText)
            setPackage(targetPackage)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    private fun isInstalled(pm: PackageManager, packageName: String): Boolean = try {
        pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (_: PackageManager.NameNotFoundException) {
        false
    }
}
