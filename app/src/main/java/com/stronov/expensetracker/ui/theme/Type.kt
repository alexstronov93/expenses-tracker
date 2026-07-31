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

/** Plus Jakarta Sans (variable font), the Duetly type family. */
val JakartaSans = FontFamily(
    jakarta(400),
    jakarta(500),
    jakarta(600),
    jakarta(700),
    jakarta(800),
)

/**
 * Named text styles from the Duetly type scale. Prefer these over the raw
 * Material typography for Duetly screens.
 */
object DuetlyType {
    val h1 = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(800),
        fontSize = 30.sp, lineHeight = 34.sp, letterSpacing = (-0.02).em,
    )
    val h2 = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(700),
        fontSize = 22.sp, lineHeight = 26.sp, letterSpacing = (-0.02).em,
    )
    val amountXl = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(800),
        fontSize = 40.sp, lineHeight = 44.sp, letterSpacing = (-0.03).em,
    )
    val amountLg = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(700),
        fontSize = 26.sp, lineHeight = 30.sp, letterSpacing = (-0.02).em,
    )
    val amountMd = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(700),
        fontSize = 18.sp, lineHeight = 22.sp, letterSpacing = (-0.01).em,
    )
    val titleMd = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(600),
        fontSize = 17.sp, lineHeight = 22.sp, letterSpacing = (-0.01).em,
    )
    val body = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(500),
        fontSize = 15.sp, lineHeight = 22.sp,
    )
    val bodyStrong = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(600),
        fontSize = 15.sp, lineHeight = 20.sp,
    )
    val small = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(500),
        fontSize = 13.sp, lineHeight = 18.sp,
    )
    /** Uppercase tracked section label. */
    val label = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(600),
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.08.em,
    )
    val pill = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(600),
        fontSize = 12.sp, lineHeight = 14.sp, letterSpacing = 0.01.em,
    )
    val navLabel = TextStyle(
        fontFamily = JakartaSans, fontWeight = FontWeight(600),
        fontSize = 11.sp, lineHeight = 13.sp,
    )
}

/** Material typography, so stray Material components still render in-family. */
val DuetlyTypography = Typography(
    bodyLarge = DuetlyType.body,
    titleMedium = DuetlyType.titleMd,
    labelSmall = DuetlyType.label,
)
