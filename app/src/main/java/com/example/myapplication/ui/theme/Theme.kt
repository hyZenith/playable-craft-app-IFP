package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

private val PlayableCraftDark = darkColorScheme(
    primary          = Accent,
    onPrimary        = TextPrimary,
    background       = Bg,
    onBackground     = TextPrimary,
    surface          = FieldFill,
    onSurface        = TextPrimary,
    onSurfaceVariant = TextLabel,
    outline          = FieldStroke,
    error            = ErrorText
)

/**
 * Global application theme.
 * Applies a calibrated global fontScale factor (0.88f) via [LocalDensity]
 * to provide a cleaner, more compact typography scale across the entire app.
 */
@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    val currentDensity = LocalDensity.current
    val compactDensity = remember(currentDensity) {
        Density(
            density = currentDensity.density,
            fontScale = currentDensity.fontScale * 0.88f
        )
    }

    CompositionLocalProvider(
        LocalDensity provides compactDensity
    ) {
        MaterialTheme(
            colorScheme = PlayableCraftDark,
            typography  = Typography,   // from Type.kt
            content     = content
        )
    }
}