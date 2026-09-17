package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

enum class AppThemeMode(val displayName: String, val description: String) {
    LIGHT("Classic Light", "Warm herbal tones, sage green & natural parchment"),
    GLASS("Glass Theme", "Translucent frosted glass, subtle glow & floating depth"),
    DARK("Dark Theme", "Botanical charcoal, eye-friendly contrast & night comfort")
}

val LocalAppThemeMode = compositionLocalOf { AppThemeMode.LIGHT }

/**
 * Universal Theme Tokens for the entire AyurGuide application.
 * When a user changes the theme (Light, Glass, Dark), these tokens automatically
 * provide the appropriate backgrounds, card surfaces, borders, text colors,
 * and container shades everywhere across all screens and components.
 */
data class AyurAppThemeColors(
    val background: Color,
    val surface: Color,
    val cardBg: Color,
    val cardBorder: Color,
    val headingText: Color,
    val primaryText: Color,
    val mutedText: Color,
    val innerTileBg: Color,
    val innerTileBorder: Color,
    val primaryBrand: Color,
    val primaryBrandDark: Color,
    val sageContainer: Color,
    val sageBorder: Color,
    val parchmentContainer: Color,
    val parchmentBorder: Color,
    val earthGold: Color,
    val terracotta: Color,
    val pittaGreen: Color,
    val isGlass: Boolean,
    val isDark: Boolean
)

val LightAyurAppThemeColors = AyurAppThemeColors(
    background = Color(0xFFFAF9F6),
    surface = Color(0xFFFFFFFF),
    cardBg = Color(0xFFFFFFFF),
    cardBorder = Color(0xFFE5E2DA),
    headingText = Color(0xFF3F4238),
    primaryText = Color(0xFF2C2C2C),
    mutedText = Color(0xFF6B705C),
    innerTileBg = Color(0xFFF3F6F1),
    innerTileBorder = Color(0xFFE5E2DA),
    primaryBrand = Color(0xFF5A5A40),
    primaryBrandDark = Color(0xFF434839),
    sageContainer = Color(0xFFDCE5D1),
    sageBorder = Color(0xFFC5D1B3),
    parchmentContainer = Color(0xFFF1E7D0),
    parchmentBorder = Color(0xFFE5DCC5),
    earthGold = Color(0xFF8C7851),
    terracotta = Color(0xFF5D4037),
    pittaGreen = Color(0xFFA7C957),
    isGlass = false,
    isDark = false
)

val DarkAyurAppThemeColors = AyurAppThemeColors(
    background = Color(0xFF131713),       // Deep Botanical Midnight
    surface = Color(0xFF1C221B),          // Dark Herb Slate
    cardBg = Color(0xFF1C221B),
    cardBorder = Color(0xFF2E382C),
    headingText = Color(0xFFF2F7EF),      // Luminous Herb Ivory
    primaryText = Color(0xFFEDEFEA),
    mutedText = Color(0xFFA8B4A4),
    innerTileBg = Color(0xFF252D24),
    innerTileBorder = Color(0xFF333E31),
    primaryBrand = Color(0xFFA7C957),     // Luminous herbal lime
    primaryBrandDark = Color(0xFF8BA646),
    sageContainer = Color(0xFF2A3727),
    sageBorder = Color(0xFF3D4F38),
    parchmentContainer = Color(0xFF2E2922),
    parchmentBorder = Color(0xFF453B2F),
    earthGold = Color(0xFFD4A373),
    terracotta = Color(0xFFE07A5F),
    pittaGreen = Color(0xFFA7C957),
    isGlass = false,
    isDark = true
)

val GlassAyurAppThemeColors = AyurAppThemeColors(
    background = Color(0xFFE5EFE9),       // Crystal Frost Sage
    surface = Color(0xEBFFFFFF),          // 92% Frosted Glass
    cardBg = Color(0xD9FFFFFF),           // 85% Translucent Glass Card
    cardBorder = Color(0x99FFFFFF),       // Reflected luminous glass border
    headingText = Color(0xFF132A1F),
    primaryText = Color(0xFF253D30),
    mutedText = Color(0xFF3D7A5C),
    innerTileBg = Color(0x8CF0F7F3),      // Frosted inner tile
    innerTileBorder = Color(0x5952B788),
    primaryBrand = Color(0xFF2D6A4F),     // Deep Emerald Glass
    primaryBrandDark = Color(0xFF1B4332),
    sageContainer = Color(0x3352B788),     // Mint Glass Frost
    sageBorder = Color(0x5952B788),
    parchmentContainer = Color(0x33D4A373),
    parchmentBorder = Color(0x59D4A373),
    earthGold = Color(0xFFD48B38),
    terracotta = Color(0xFFBC6C25),
    pittaGreen = Color(0xFF40916C),
    isGlass = true,
    isDark = false
)

val LocalAyurAppThemeColors = compositionLocalOf { LightAyurAppThemeColors }

object AyurTheme {
    val colors: AyurAppThemeColors
        @Composable
        get() = LocalAyurAppThemeColors.current

    val mode: AppThemeMode
        @Composable
        get() = LocalAppThemeMode.current
}

private val NaturalLightColorScheme = lightColorScheme(
    primary = Color(0xFF5A5A40),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDCE5D1),
    onPrimaryContainer = Color(0xFF3F4238),
    secondary = Color(0xFF6B705C),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDCE5D1),
    onSecondaryContainer = Color(0xFF434839),
    tertiary = Color(0xFF8C7851),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF1E7D0),
    onTertiaryContainer = Color(0xFF5D4037),
    background = Color(0xFFFAF9F6),
    onBackground = Color(0xFF2C2C2C),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF2C2C2C),
    surfaceVariant = Color(0xFFF1E7D0),
    onSurfaceVariant = Color(0xFF5D4037),
    outline = Color(0xFFE5E2DA),
    outlineVariant = Color(0xFFE5DCC5)
)

private val NaturalDarkColorScheme = darkColorScheme(
    primary = Color(0xFFA7C957), // Pitta Lime/Herb Green
    onPrimary = Color(0xFF141913),
    primaryContainer = Color(0xFF2A3727),
    onPrimaryContainer = Color(0xFFE3EDE0),
    secondary = Color(0xFFA8B4A4),
    onSecondary = Color(0xFF141913),
    secondaryContainer = Color(0xFF252F23),
    onSecondaryContainer = Color(0xFFDCE5D1),
    tertiary = Color(0xFFD4A373),
    onTertiary = Color(0xFF141913),
    background = Color(0xFF131713), // Deep Botanical Midnight
    onBackground = Color(0xFFEDEFEA),
    surface = Color(0xFF1C221B),   // Dark Herb Slate
    onSurface = Color(0xFFEDEFEA),
    surfaceVariant = Color(0xFF252D24),
    onSurfaceVariant = Color(0xFFB5C2B2),
    outline = Color(0xFF333E31),
    outlineVariant = Color(0xFF222920)
)

private val NaturalGlassColorScheme = lightColorScheme(
    primary = Color(0xFF2D6A4F),         // Deep Emerald Glass
    onPrimary = Color.White,
    primaryContainer = Color(0x3352B788), // Frosted Mint
    onPrimaryContainer = Color(0xFF081C15),
    secondary = Color(0xFF40916C),
    onSecondary = Color.White,
    secondaryContainer = Color(0x2674C69D),
    onSecondaryContainer = Color(0xFF1B4332),
    tertiary = Color(0xFFD48B38),
    onTertiary = Color.White,
    background = Color(0xFFEFF5F1),       // Crystal Frost Sage
    onBackground = Color(0xFF1B2A22),
    surface = Color(0xE6FFFFFF),          // 90% Frosted Glass
    onSurface = Color(0xFF1B2A22),
    surfaceVariant = Color(0xB3FFFFFF),   // 70% Semi-translucent Glass
    onSurfaceVariant = Color(0xFF2D473A),
    outline = Color(0x99FFFFFF),          // Reflected Luminous Glass Edge
    outlineVariant = Color(0x4052B788)
)

@Composable
fun MyApplicationTheme(
    themeMode: AppThemeMode = AppThemeMode.LIGHT,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val appThemeColors = when (themeMode) {
        AppThemeMode.LIGHT -> LightAyurAppThemeColors
        AppThemeMode.DARK -> DarkAyurAppThemeColors
        AppThemeMode.GLASS -> GlassAyurAppThemeColors
    }

    val colorScheme = when (themeMode) {
        AppThemeMode.LIGHT -> NaturalLightColorScheme
        AppThemeMode.DARK -> NaturalDarkColorScheme
        AppThemeMode.GLASS -> NaturalGlassColorScheme
    }

    CompositionLocalProvider(
        LocalAppThemeMode provides themeMode,
        LocalAyurAppThemeColors provides appThemeColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
