package com.stronov.expensetracker.ui.theme

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/** Accessor for the current palette: `Duetly.colors.success`, etc. */
object Duetly {
    val colors: DuetlyColors
        @Composable get() = LocalDuetlyColors.current
}

/** Motion tokens: one calm easing curve, three durations. */
object DuetlyMotion {
    const val FAST = 120
    const val BASE = 220
    const val SLOW = 420
}

@Composable
fun DuetlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val target = if (darkTheme) DarkDuetlyColors else LightDuetlyColors

    // Cross-fade the palette so the theme switch reads as calm, not abrupt.
    // Each call has to sit directly in the composable body — a local helper
    // function cannot invoke @Composable APIs.
    val spec = tween<Color>(DuetlyMotion.BASE)
    val appBg by animateColorAsState(target.appBg, spec, label = "appBg")
    val surface by animateColorAsState(target.surface, spec, label = "surface")
    val sunken by animateColorAsState(target.sunken, spec, label = "sunken")
    val textPrimary by animateColorAsState(target.textPrimary, spec, label = "textPrimary")
    val textSecondary by animateColorAsState(target.textSecondary, spec, label = "textSecondary")
    val border by animateColorAsState(target.border, spec, label = "border")

    val duetly = target.copy(
        appBg = appBg,
        surface = surface,
        sunken = sunken,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        border = border,
    )

    val material = if (darkTheme) {
        darkColorScheme(
            primary = duetly.actionPrimary, onPrimary = duetly.onActionPrimary,
            background = duetly.appBg, onBackground = duetly.textPrimary,
            surface = duetly.surface, onSurface = duetly.textPrimary,
            surfaceVariant = duetly.sunken, onSurfaceVariant = duetly.textSecondary,
            outline = duetly.border, error = duetly.danger,
        )
    } else {
        lightColorScheme(
            primary = duetly.actionPrimary, onPrimary = duetly.onActionPrimary,
            background = duetly.appBg, onBackground = duetly.textPrimary,
            surface = duetly.surface, onSurface = duetly.textPrimary,
            surfaceVariant = duetly.sunken, onSurfaceVariant = duetly.textSecondary,
            outline = duetly.border, error = duetly.danger,
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalDuetlyColors provides duetly) {
        MaterialTheme(colorScheme = material, typography = DuetlyTypography, content = content)
    }
}
