package com.stronov.expensetracker.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/** Accessor for the current Duetly palette: `Duetly.colors.positive`, etc. */
object Duetly {
    val colors: DuetlyColors
        @Composable get() = LocalDuetlyColors.current
}

@Composable
fun DuetlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val duetly = if (darkTheme) DarkDuetlyColors else LightDuetlyColors

    // Map the Duetly tokens onto a Material 3 scheme so stray Material
    // components (ripples, text selection, etc.) stay on-brand.
    val material = if (darkTheme) {
        darkColorScheme(
            primary = duetly.partnerA,
            onPrimary = duetly.onInkSurface,
            background = duetly.appBg,
            onBackground = duetly.textPrimary,
            surface = duetly.surface,
            onSurface = duetly.textPrimary,
            surfaceVariant = duetly.sunken,
            onSurfaceVariant = duetly.textSecondary,
            outline = duetly.border,
            error = duetly.danger,
        )
    } else {
        lightColorScheme(
            primary = duetly.partnerA,
            onPrimary = duetly.onInkSurface,
            background = duetly.appBg,
            onBackground = duetly.textPrimary,
            surface = duetly.surface,
            onSurface = duetly.textPrimary,
            surfaceVariant = duetly.sunken,
            onSurfaceVariant = duetly.textSecondary,
            outline = duetly.border,
            error = duetly.danger,
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = duetly.appBg.toArgb()
            window.navigationBarColor = duetly.appBg.toArgb()
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalDuetlyColors provides duetly) {
        MaterialTheme(
            colorScheme = material,
            typography = DuetlyTypography,
            content = content,
        )
    }
}
