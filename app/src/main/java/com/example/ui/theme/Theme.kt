package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = IndigoPrimary,
  onPrimary = Color.White,
  primaryContainer = StudioCardDarkElevated,
  onPrimaryContainer = TextPrimary,
  secondary = CyanAccent,
  onSecondary = Color.Black,
  secondaryContainer = StudioCardDark,
  onSecondaryContainer = TextPrimary,
  tertiary = AmberAccent,
  onTertiary = Color.Black,
  background = StudioDark,
  onBackground = TextPrimary,
  surface = StudioCardDark,
  onSurface = TextPrimary,
  surfaceVariant = StudioCardDarkElevated,
  onSurfaceVariant = TextSecondary,
  outline = StudioBorder
)

@Composable
fun VideoStudioTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content
  )
}
