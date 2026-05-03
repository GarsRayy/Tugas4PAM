package org.garis.pam

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import org.garis.pam.navigation.AppNavigation
import org.koin.compose.koinInject
import org.garis.pam.viewmodel.ProfileViewModel
import org.garis.pam.viewmodel.SettingsViewModel

@Composable
fun App() {
    val profileViewModel: ProfileViewModel = koinInject()
    val settingsViewModel: SettingsViewModel = koinInject()

    val profileUiState by profileViewModel.uiState.collectAsState()
    val currentTheme by settingsViewModel.currentTheme.collectAsState()

    val glassColors = remember(currentTheme) {
        if (currentTheme.startsWith("ai_theme|")) {
            try {
                val parts = currentTheme.split("|")
                val primary = parts[1].toColor()
                val secondary = parts[2].toColor()
                val accent = parts[3].toColor()
                val background = parts[4].toColor()
                
                DarkGlassColors.copy(
                    BgPage = background,
                    BgPhone = background.copy(alpha = 0.9f),
                    BgHeroTop = primary,
                    GlassBg = accent.copy(alpha = 0.15f),
                    GlassBorder = accent.copy(alpha = 0.3f),
                    TextSecond = secondary
                )
            } catch (e: Exception) {
                AuroraGlassColors
            }
        } else {
            when (currentTheme) {
                "light" -> LightGlassColors
                "dark" -> DarkGlassColors
                else -> AuroraGlassColors
            }
        }
    }

    val isDark = currentTheme != "light"
    val materialScheme = if (isDark) darkColorScheme() else lightColorScheme()

    CompositionLocalProvider(LocalGlassColors provides glassColors) {
        MaterialTheme(colorScheme = materialScheme) {
            AppNavigation(
                profileViewModel = profileViewModel,
                isDarkMode       = isDark
            )
        }
    }
}
