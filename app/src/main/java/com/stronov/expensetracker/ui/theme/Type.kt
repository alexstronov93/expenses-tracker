package com.stronov.expensetracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.stronov.expensetracker.R

// The variationSettings overload drives the variable font's weight axis.
@OptIn(ExperimentalTextApi::class)
private fun jakarta(weight: Int) = Font(
    resId = R.font.plus_jakarta_sans,
    weight = FontWeight(weight),
    variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
)

/** Plus Jakarta Sans (variable), the single Duetly typeface. */
val JakartaSans = FontFamily(
    jakarta(400), jakarta(500), jakarta(600), jakarta(700), jakarta(800),
)

// Tabular figures keep columns of amounts aligned — a 2a ("app-project chrome") trait.
private const val TNUM = "tnum"

/**
 * The Duetly type scale. Tight tracking on headlines, near-normal on body,
 * tracked-up uppercase section labels in Slate.
 */
object DuetlyType {
    val screenTitle = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(800),
        fontSize = 30.sp, lineHeight = 34.sp, letterSpacing = (-0.02).em,
    )
    val amountHero = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(800),
        fontSize = 38.sp, lineHeight = 42.sp, letterSpacing = (-0.03).em,
        fontFeatureSettings = TNUM,
    )
    val amountLg = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(700),
        fontSize = 20.sp, lineHeight = 24.sp, letterSpacing = (-0.01).em,
        fontFeatureSettings = TNUM,
    )
    val amountMd = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(700),
        fontSize = 16.sp, lineHeight = 20.sp,
        fontFeatureSettings = TNUM,
    )
    val amountSm = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(600),
        fontSize = 14.sp, lineHeight = 18.sp,
        fontFeatureSettings = TNUM,
    )
    val titleMd = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(600),
        fontSize = 17.sp, lineHeight = 22.sp, letterSpacing = (-0.01).em,
    )
    val body = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(500),
        fontSize = 16.sp, lineHeight = 22.sp, letterSpacing = (-0.005).em,
    )
    val bodyStrong = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(600),
        fontSize = 15.sp, lineHeight = 20.sp,
    )
    val small = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(500),
        fontSize = 14.sp, lineHeight = 19.sp,
    )
    val caption = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(500),
        fontSize = 13.sp, lineHeight = 17.sp,
        fontFeatureSettings = TNUM,
    )
    /** Uppercase, tracked section label — the 2a chrome signature. */
    val sectionLabel = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(700),
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.1.em,
    )
    val navLabel = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(600),
        fontSize = 11.sp, lineHeight = 13.sp,
    )
    val chip = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(600),
        fontSize = 13.sp, lineHeight = 16.sp,
    )
}

/** Material typography, so stray Material components stay in-family. */
val DuetlyTypography = Typography(
    bodyLarge = DuetlyType.body,
    titleMedium = DuetlyType.titleMd,
    labelSmall = DuetlyType.sectionLabel,
)
