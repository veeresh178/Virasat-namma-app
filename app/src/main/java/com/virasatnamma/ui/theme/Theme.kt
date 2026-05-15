package com.virasatnamma.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Premium Heritage Color Scheme for Virasat-Namma
 */
private val HeritageLightColorScheme = lightColorScheme(
    primary = VirasatColors.DeepSaffron,
    onPrimary = VirasatColors.White,
    primaryContainer = VirasatColors.AntiqueGold,
    onPrimaryContainer = VirasatColors.TempleBrown,
    secondary = VirasatColors.Copper,
    onSecondary = VirasatColors.White,
    secondaryContainer = VirasatColors.Sandstone,
    onSecondaryContainer = VirasatColors.TempleBrown,
    tertiary = VirasatColors.DarkMaroon,
    onTertiary = VirasatColors.White,
    background = VirasatColors.SoftCream,
    onBackground = VirasatColors.TempleBrown,
    surface = VirasatColors.Parchment,
    onSurface = VirasatColors.TempleBrown,
    surfaceVariant = VirasatColors.Sandstone.copy(alpha = 0.2f),
    onSurfaceVariant = VirasatColors.StoneGray,
    outline = VirasatColors.Brass
)

private val HeritageDarkColorScheme = darkColorScheme(
    primary = VirasatColors.AntiqueGold,
    onPrimary = VirasatColors.TempleBrown,
    primaryContainer = VirasatColors.DeepSaffron,
    onPrimaryContainer = VirasatColors.White,
    secondary = VirasatColors.Copper,
    onSecondary = Color.Black,
    background = VirasatColors.Black,
    onBackground = VirasatColors.SoftCream,
    surface = VirasatColors.DarkGray,
    onSurface = VirasatColors.SoftCream,
    surfaceVariant = VirasatColors.StoneGray,
    onSurfaceVariant = VirasatColors.Sandstone
)

@Composable
fun VirasatNammaTheme(
    darkTheme: Boolean = false, // Can be connected to system settings later
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) HeritageDarkColorScheme else HeritageLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VirasatTypography,
        content = content
    )
}
