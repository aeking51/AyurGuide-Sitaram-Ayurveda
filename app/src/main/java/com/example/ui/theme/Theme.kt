package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NaturalLightColorScheme = lightColorScheme(
    primary = NaturalMossPrimary,
    onPrimary = NaturalCardSurface,
    primaryContainer = NaturalSageContainer,
    onPrimaryContainer = NaturalTextHeading,
    secondary = NaturalOliveMuted,
    onSecondary = NaturalCardSurface,
    secondaryContainer = NaturalSageContainer,
    onSecondaryContainer = NaturalMossDark,
    tertiary = NaturalEarthGold,
    onTertiary = NaturalCardSurface,
    tertiaryContainer = NaturalParchmentContainer,
    onTertiaryContainer = NaturalTerracotta,
    background = NaturalBackground,
    onBackground = NaturalTextPrimary,
    surface = NaturalCardSurface,
    onSurface = NaturalTextPrimary,
    surfaceVariant = NaturalParchmentContainer,
    onSurfaceVariant = NaturalTerracotta,
    outline = NaturalCardBorder,
    outlineVariant = NaturalParchmentBorder
)

private val NaturalDarkColorScheme = darkColorScheme(
    primary = NaturalSageContainer,
    onPrimary = NaturalMossDark,
    primaryContainer = NaturalMossPrimary,
    onPrimaryContainer = NaturalSageContainer,
    secondary = NaturalOliveMuted,
    onSecondary = NaturalBackground,
    secondaryContainer = NaturalMossDark,
    onSecondaryContainer = NaturalSageContainer,
    tertiary = NaturalEarthGold,
    onTertiary = NaturalBackground,
    background = NaturalTextHeading,
    onBackground = NaturalBackground,
    surface = NaturalMossDark,
    onSurface = NaturalBackground,
    surfaceVariant = NaturalMossPrimary,
    onSurfaceVariant = NaturalParchmentContainer,
    outline = NaturalOliveMuted
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) NaturalDarkColorScheme else NaturalLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
