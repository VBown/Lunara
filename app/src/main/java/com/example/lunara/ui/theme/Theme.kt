package com.example.lunara.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val DarkColorScheme = darkColorScheme(
    primary = CoralEnergeticoDark,
    onPrimary = VerdeSalviaDark,
    secondary = LilaSerenoDark,
    onSecondary = VerdeSalviaDark,
    tertiary = DoradoSuaveDark,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = VerdeSalviaDark,
    onSurface = LilaLunaraDark,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = VerdeSalviaDark
)

private val LightColorScheme = lightColorScheme(
    primary = CoralEnergetico,       // Botones principales, acciones
    onPrimary = BlancoHueso,
    secondary = LilaMistico,          // Elementos secundarios, énfasis suave
    onSecondary = CremaSuave,
    tertiary = DoradoSuave,          // Acentos, iconos
    background = BlancoHueso,        // Fondo general de la app (Blanco Hueso)
    onBackground = LilaLunara,      // Color por defecto del texto en fondos
    surface = BlancoHueso,           // Superficies estándar
    onSurface = LilaLunara,         // Color por defecto del texto en superficies

    // Cards y TextFields según marca**
    surfaceVariant = LilaLunara, // Lila  para tarjetas
    onSurfaceVariant = VioletaGris, // Color del texto dentro de surfaceVariant

    // Colores para los OutlineTextFields
    outline = LilaMistico, // Borde de TextField
    primaryContainer = CoralVital, // Color de relleno de TextField (si aplica, o para énfasis)
    onPrimaryContainer = VioletaPalido, // Texto en primaryContainer

    error = Color(0xFFBA1A1A)
)

@Composable
fun LunaraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}