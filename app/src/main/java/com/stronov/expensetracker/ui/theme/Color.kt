package com.stronov.expensetracker.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Duetly design tokens, transcribed from the design system's `tokens/colors.css`.
 *
 * Brand rule: **Cobalt and Coral are reserved for people and their data**
 * (partner avatars, splits, contributions). Chrome — buttons, dividers,
 * structural UI — is always Ink or neutral.
 */
@Immutable
data class DuetlyColors(
    val appBg: Color,
    val surface: Color,
    val sunken: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val border: Color,
    val borderStrong: Color,
    // Partner identity (people only)
    val partnerA: Color,
    val partnerASoft: Color,
    val partnerB: Color,
    val partnerBSoft: Color,
    // Semantic
    val success: Color,
    val successSoft: Color,
    val successInk: Color,
    val warning: Color,
    val warningSoft: Color,
    val warningTile: Color,
    val warningInk: Color,
    val danger: Color,
    val dangerSoft: Color,
    val dangerInk: Color,
    // Segments of the spent / held / safe bar
    val segmentSpent: Color,
    val segmentHeld: Color,
    val segmentSafe: Color,
    // Safe-to-spend gauge
    val gaugeTrack: Color,
    val gaugeFill: Color,
    val gaugeThumb: Color,
    // Emphasis surface: FAB and primary action
    val actionPrimary: Color,
    val onActionPrimary: Color,
    val isDark: Boolean,
)

val LightDuetlyColors = DuetlyColors(
    appBg = Color(0xFFF7F8FA),          // --chalk
    surface = Color(0xFFFFFFFF),        // --white
    sunken = Color(0xFFEFF1F4),         // --bg-sunken
    textPrimary = Color(0xFF16181F),    // --ink
    textSecondary = Color(0xFF8A8F99),  // --slate
    border = Color(0xFFE7E9ED),         // --line
    borderStrong = Color(0xFFE4E6EA),   // --slate-soft
    partnerA = Color(0xFF3B5BDB),       // --cobalt
    partnerASoft = Color(0xFFE9EDFB),
    partnerB = Color(0xFFFB6F5B),       // --coral
    partnerBSoft = Color(0xFFFEEBE7),
    success = Color(0xFF17A87F),
    successSoft = Color(0xFFE7F6F1),
    successInk = Color(0xFF0E7256),
    warning = Color(0xFFF0733C),
    warningSoft = Color(0xFFFDEDE3),
    warningTile = Color(0xFFF9DAC8),
    warningInk = Color(0xFFB4501C),
    danger = Color(0xFFD3453B),
    dangerSoft = Color(0xFFFBECEA),
    dangerInk = Color(0xFFA32C24),
    segmentSpent = Color(0xFF16181F),
    segmentHeld = Color(0xFF8A8F99),
    segmentSafe = Color(0xFFE4E6EA),
    gaugeTrack = Color(0xFF16181F),
    gaugeFill = Color(0xFF21AB93),
    gaugeThumb = Color(0xFFC2C6CE),
    actionPrimary = Color(0xFF16181F),
    onActionPrimary = Color(0xFFFFFFFF),
    isDark = false,
)

/** Dark values follow the prototype's dark theme block. */
val DarkDuetlyColors = DuetlyColors(
    appBg = Color(0xFF101115),
    surface = Color(0xFF16181F),
    sunken = Color(0xFF24262F),
    textPrimary = Color(0xFFF2F3F5),
    textSecondary = Color(0xFF9AA0AB),
    border = Color(0xFF2A2D36),
    borderStrong = Color(0xFF363945),
    partnerA = Color(0xFF7B93F0),
    partnerASoft = Color(0xFF20263C),
    partnerB = Color(0xFFFF9182),
    partnerBSoft = Color(0xFF3A241F),
    success = Color(0xFF2FBFA5),
    successSoft = Color(0xFF17352F),
    successInk = Color(0xFF7FDCC4),
    warning = Color(0xFFFF8F3C),
    warningSoft = Color(0xFF2E2114),
    warningTile = Color(0xFF43301C),
    warningInk = Color(0xFFF2B57C),
    danger = Color(0xFFF0645A),
    dangerSoft = Color(0xFF2E1512),
    dangerInk = Color(0xFFFF9C94),
    segmentSpent = Color(0xFFF2F3F5),
    segmentHeld = Color(0xFF6B7180),
    segmentSafe = Color(0xFF363945),
    gaugeTrack = Color(0xFFF2F3F5),
    gaugeFill = Color(0xFF2FBFA5),
    gaugeThumb = Color(0xFF6B7180),
    actionPrimary = Color(0xFFF2F3F5),
    onActionPrimary = Color(0xFF101115),
    isDark = true,
)

val LocalDuetlyColors = staticCompositionLocalOf { LightDuetlyColors }
