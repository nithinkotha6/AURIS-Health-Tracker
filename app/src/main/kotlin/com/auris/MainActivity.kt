package com.auris

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.auris.navigation.AurisNavHost
import com.auris.navigation.Screen
import com.auris.ui.theme.AurisTheme
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * MainActivity — single activity host for the entire AURIS app.
 * Edge-to-edge, Compose content, onNewIntent for auris:// deep links.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()
            AurisTheme {
                AurisNavHost(navController = navController)
            }

            // Handle initial intent
            LaunchedEffect(intent) {
                handleIntent(intent, navController)
            }
        }
    }

    /** Handles auris:// deep links when app is already running (singleTask) */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Intent will be handled by LaunchedEffect(intent) in setContent
    }

    private fun handleIntent(intent: Intent?, navController: androidx.navigation.NavHostController) {
        val intent = intent ?: return
        
        when (intent.action) {
            Intent.ACTION_SEND -> {
                if (intent.type == "text/plain") {
                    val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                    if (sharedText != null) {
                        val encoded = URLEncoder.encode(sharedText, StandardCharsets.UTF_8.toString())
                        navController.navigate(Screen.SharedText.createRoute(encoded))
                    }
                }
            }
            Intent.ACTION_VIEW -> {
                val data = intent.data
                if (data?.scheme == "auris") {
                    // Navigation component handles this via deepLinks { uriPattern = ... }
                    // but we can manually navigate if needed or just let it be.
                    // If it's already at the log screen, we might need to update it.
                }
            }
        }
    }
}
