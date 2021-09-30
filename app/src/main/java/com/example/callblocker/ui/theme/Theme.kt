package com.example.callblocker.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = CallBlockerWhite,
    primaryVariant = CallBlockerWhite,
    secondary = CallBlockerWhite,
    secondaryVariant = CallBlockerWhite,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    surface = CallBlockerDarkSurface,
    background = CallBlockerDarkSurface,
    onSurface = Color.White,
    onBackground = Color.White
)

@Composable
fun CallBlockerTheme(content: @Composable () -> Unit) {
    val colors = DarkColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
