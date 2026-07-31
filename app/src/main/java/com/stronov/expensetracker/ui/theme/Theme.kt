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
    fun anim(c: Color) = animateColorAsState(c, tween(DuetlyMotion.BASE), label = "color").value
    val duetly = target.copy(
        appBg = anim(target.appBg),
        surface = anim(target.surface),
        sunken = anim(target.sunken),
        textPrimary = anim(target.textPrimary),
        textSecondary = anim(target.textSecondary),
        border = anim(target.border),
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
