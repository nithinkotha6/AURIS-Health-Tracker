package com.auris.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * AurisColors — Apple iOS 17 semantic color system (light mode).
 * Values sourced directly from Apple HIG / UIColor documentation.
 */
object AurisColors {

    // ── System Backgrounds ────────────────────────────────────────────
    val BgPrimary   = Color(0xFFF2F2F7)   // systemGroupedBackground
    val BgSecondary = Color(0xFFFFFFFF)   // secondarySystemGroupedBackground (cards)
    val BgTertiary  = Color(0xFFEFEFF4)   // tertiarySystemGroupedBackground

    // ── Labels ────────────────────────────────────────────────────────
    val LabelPrimary     = Color(0xFF000000)
    val LabelSecondary   = Color(0x993C3C43)   // rgba(60,60,67,0.60)
    val LabelTertiary    = Color(0x4D3C3C43)   // rgba(60,60,67,0.30)
    val LabelQuaternary  = Color(0x2E3C3C43)   // rgba(60,60,67,0.18)

    // ── Fills ─────────────────────────────────────────────────────────
    val FillPrimary   = Color(0x33787880)   // rgba(120,120,128,0.20)
    val FillSecondary = Color(0x29787880)   // rgba(120,120,128,0.16)
    val FillTertiary  = Color(0x1F767680)   // rgba(118,118,128,0.12)

    // ── Separators ────────────────────────────────────────────────────
    val Separator       = Color(0x1F3C3C43)   // rgba(60,60,67,0.12)
    val SeparatorOpaque = Color(0xFFC6C6C8)

    // ── System / Brand Colors ─────────────────────────────────────────
    val Blue   = Color(0xFF007AFF)
    val Green  = Color(0xFF34C759)
    val Green2 = Color(0xFF30D158)
    val Orange = Color(0xFFFF9500)
    val Red    = Color(0xFFFF3B30)
    val Red2   = Color(0xFFFF2D55)
    val Teal   = Color(0xFF5AC8FA)
    val Purple = Color(0xFFAF52DE)
    val Indigo = Color(0xFF5856D6)
    val Yellow = Color(0xFFFFD60A)
    val Pink   = Color(0xFFFF2D55)

    // ── Nav Bar / Materials ───────────────────────────────────────────
    val NavBg           = Color(0xF0F9F9F9)   // rgba(249,249,249,0.94)
    val MaterialThick   = Color(0xD9FFFFFF)   // rgba(255,255,255,0.85)
    val MaterialRegular = Color(0xB8FFFFFF)   // rgba(255,255,255,0.72)
    val MaterialThin    = Color(0x8CFFFFFF)   // rgba(255,255,255,0.55)

    // ── Glass Card Surfaces ───────────────────────────────────────────
    val CardGlass         = Color(0xB8FFFFFF)   // 0.72 opacity
    val CardGlassElevated = Color(0xE0FFFFFF)   // 0.88 opacity
    val CardBorder        = Color(0xCCFFFFFF)   // 0.80 opacity
    val CardBorderHigh    = Color(0xE5FFFFFF)   // 0.90 opacity

    // ── Shadow colors (for elevation modeling) ────────────────────────
    // Used via Modifier.shadow(); note Android shadows are less precise than CSS
    val ShadowColor = Color(0xFF000000)

    // ── Liquid Glass Tab Bar ─────────────────────────────────────────
    val GlassTabBg        = Color(0x59FFFFFF)   // rgba(255,255,255,0.35)
    val GlassTabActive    = Color(0x73FFFFFF)   // rgba(255,255,255,0.45)
    val GlassTabBorderTop = Color(0xE6FFFFFF)   // rgba(255,255,255,0.90)

    // ── Pastel gradient (AI search bar) ─────────────────────────────
    val PastelBlue   = Color(0xFFA7C7E7)
    val PastelPurple = Color(0xFFD8B4E2)
    val PastelGreen  = Color(0xFFA8E6CF)

    // ── FAB shadow ────────────────────────────────────────────────────
    val ShadowFab = Color(0x66007AFF)   // rgba(0,122,255,0.40)

    // ── Backward-compatible aliases (used in DeficiencyLevel.kt) ─────
    val StatusOptimal   = Green
    val StatusAdequate  = Blue
    val StatusLow       = Orange
    val StatusDeficient = Red
    val StatusCritical  = Red2

    // ── Pct-based semantic color helper ──────────────────────────────
    fun pctColor(pct: Int): Color = when {
        pct >= 80 -> Green
        pct >= 60 -> Blue
        pct >= 40 -> Orange
        else      -> Red
    }
}
