package com.stronov.expensetracker.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Duetly design tokens, translated from the Figma prototype's CSS variables.
 * Two palettes — light and dark — are exposed through [DuetlyColors] and made
 * available to composables via [LocalDuetlyColors].
 */
@Immutable
data class DuetlyColors(
    val appBg: Color,
    val surface: Color,
    val sunken: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textFaint: Color,
    val border: Color,
    val borderStrong: Color,
    // Partners / brand
    val partnerA: Color,       // cobalt
    val partnerASoft: Color,
    val partnerB: Color,       // coral
    val partnerBSoft: Color,
    // Semantic
    val positive: Color,       // teal — gauge fill, positive amounts
    val positiveSoft: Color,
    val paidText: Color,       // green — "Paid" pill
    val paidSoft: Color,
    val warning: Color,        // coral — "Due" pill, alert accent
    val warningSoft: Color,    // peach — alert card background
    val danger: Color,
    val dangerSoft: Color,
    // Gauge
    val gaugeTrack: Color,
    val gaugeFill: Color,
    val gaugeThumb: Color,
    // Emphasis surface (FAB, primary button)
    val inkSurface: Color,     // near-black chip/FAB in light, white in dark
    val onInkSurface: Color,
    val isDark: Boolean,
)

val LightDuetlyColors = DuetlyColors(
    appBg = Color(0xFFF7F8FA),
    surface = Color(0xFFFFFFFF),
    sunken = Color(0xFFEFF1F4),
    textPrimary = Color(0xFF16181F),
    textSecondary = Color(0xFF8A8F99),
    textFaint = Color(0x9916181F),
    border = Color(0xFFE7E9ED),
    borderStrong = Color(0xFFE4E6EA),
    partnerA = Color(0xFF3B5BDB),
    partnerASoft = Color(0xFFE9EDFB),
    partnerB = Color(0xFFFB6F5B),
    partnerBSoft = Color(0xFFFEEBE7),
    positive = Color(0xFF21AB93),
    positiveSoft = Color(0xFFD7EDE3),
    paidText = Color(0xFF2E9E6B),
    paidSoft = Color(0xFFE9F6F0),
    warning = Color(0xFFC97A1E),
    warningSoft = Color(0xFFFDF3E4),
    danger = Color(0xFFD3453B),
    dangerSoft = Color(0xFFFBECEA),
    gaugeTrack = Color(0xFF16181F),
    gaugeFill = Color(0xFF21AB93),
    gaugeThumb = Color(0xFFC2C6CE),
    inkSurface = Color(0xFF16181F),
    onInkSurface = Color(0xFFFFFFFF),
    isDark = false,
)

val DarkDuetlyColors = DuetlyColors(
    appBg = Color(0xFF101115),
    surface = Color(0xFF16181F),
    sunken = Color(0xFF24262F),
    textPrimary = Color(0xFFF2F3F5),
    textSecondary = Color(0xFF9AA0AB),
    textFaint = Color(0x99F2F3F5),
    border = Color(0xFF2A2D36),
    borderStrong = Color(0xFF363945),
    partnerA = Color(0xFF7B93F0),
    partnerASoft = Color(0xFF20263C),
    partnerB = Color(0xFFFF9182),
    partnerBSoft = Color(0xFF3A241F),
    positive = Color(0xFF2FBFA5),
    positiveSoft = Color(0xFF17352F),
    paidText = Color(0xFF45C08A),
    paidSoft = Color(0xFF17352F),
    warning = Color(0xFFF2B57C),
    warningSoft = Color(0xFF2E2114),
    danger = Color(0xFFF0645A),
    dangerSoft = Color(0xFF2E1512),
    gaugeTrack = Color(0xFFF2F3F5),
    gaugeFill = Color(0xFF2FBFA5),
    gaugeThumb = Color(0xFF5B606B),
    inkSurface = Color(0xFFFFFFFF),
    onInkSurface = Color(0xFF101115),
    isDark = true,
)

val LocalDuetlyColors = staticCompositionLocalOf { LightDuetlyColors }
