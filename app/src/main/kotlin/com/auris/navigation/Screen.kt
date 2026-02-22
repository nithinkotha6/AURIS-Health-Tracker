package com.auris.navigation

/**
 * Screen — sealed hierarchy of all navigation routes in AURIS.
 */
sealed class Screen(val route: String) {

    // ── Main tabs ───────────────────────────────────────────────────────
    object Home     : Screen("home")
    object Log      : Screen("log")
    object Diary    : Screen("diary")
    object Overview : Screen("overview")
    object Profile  : Screen("profile")

    // ── Deep link receiver: AI food log confirmation (Phase 8) ──────────
    object Confirmation : Screen("confirmation/{encodedUri}") {
        fun createRoute(encodedUri: String) = "confirmation/$encodedUri"
        const val ARG_ENCODED_URI = "encodedUri"
    }

    // ── Shared text receiver (Phase 8 fallback) ─────────────────────────
    object SharedText : Screen("sharedText/{encodedText}") {
        fun createRoute(encodedText: String) = "sharedText/$encodedText"
        const val ARG_ENCODED_TEXT = "encodedText"
    }

    // ── Nutrient detail (Phase 2+) ──────────────────────────────────────
    object VitaminDetail : Screen("vitaminDetail/{nutrientId}") {
        fun createRoute(nutrientId: String) = "vitaminDetail/$nutrientId"
        const val ARG_NUTRIENT_ID = "nutrientId"
    }
}
