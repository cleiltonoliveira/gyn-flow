package com.cleios.gynflow.ui.theme
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

// ---------- Light Colors ----------
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0D47A1),       // Deep Blue
    onPrimary = Color.White,

    secondary = Color(0xFF00C853),     // Green Accent
    onSecondary = Color.Black,

    tertiary = Color(0xFFFFAB00),      // Amber
    onTertiary = Color.Black,

    background = Color(0xFFF7F9FC),    // Light Gray-Blue
    onBackground = Color(0xFF1B1C1E),

    surface = Color.White,
    onSurface = Color(0xFF1B1C1E),

    error = Color(0xFFD32F2F),
    onError = Color.White
)

// ---------- Dark Colors ----------
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF82B1FF),       // Lighter Blue
    onPrimary = Color.Black,

    secondary = Color(0xFF69F0AE),     // Light Green
    onSecondary = Color.Black,

    tertiary = Color(0xFFFFD740),      // Bright Amber
    onTertiary = Color.Black,

    background = Color(0xFF121212),    // True Dark
    onBackground = Color.White,

    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,

    error = Color(0xFFEF9A9A),
    onError = Color.Black
)

// ---------- Theme Setup ----------
@Composable
fun GynFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme)
                dynamicDarkColorScheme(context)
            else
                dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
