package com.auris

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.auris.navigation.AurisNavHost
import com.auris.ui.theme.AurisTheme
import dagger.hilt.android.AndroidEntryPoint

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
            AurisTheme {
                val navController = rememberNavController()
                AurisNavHost(navController = navController)
            }
        }
    }

    /** Handles auris:// deep links when app is already running (singleTask) */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Phase 8: parse intent and navigate to ConfirmationScreen
    }
}
