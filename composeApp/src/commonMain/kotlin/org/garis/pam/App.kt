package org.garis.pam

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import org.garis.pam.data.local.DatabaseDriverFactory
import org.garis.pam.data.local.SettingsManager
import org.garis.pam.navigation.AppNavigation
import org.garis.pam.viewmodel.NoteViewModel
import org.garis.pam.viewmodel.ProfileViewModel
import org.garis.pam.viewmodel.SettingsViewModel

@Composable
fun App(databaseDriverFactory: DatabaseDriverFactory) {
    val profileViewModel: ProfileViewModel = viewModel { ProfileViewModel() }
    val settingsManager = remember { SettingsManager() }
    val settingsViewModel: SettingsViewModel = viewModel { SettingsViewModel(settingsManager) }

    val profileUiState by profileViewModel.uiState.collectAsState()
    val currentTheme by settingsViewModel.currentTheme.collectAsState()

    val glassColors = when (currentTheme) {
        "light" -> LightGlassColors
        "dark" -> DarkGlassColors
        else -> AuroraGlassColors // "aurora_glass" default
    }

    val isDark = currentTheme != "light"
    val materialScheme = if (isDark) darkColorScheme() else lightColorScheme()

    CompositionLocalProvider(LocalGlassColors provides glassColors) {
        MaterialTheme(colorScheme = materialScheme) {
            AppNavigation(
                profileViewModel = profileViewModel,
                isDarkMode       = isDark,
                onToggleDark     = { 
                    val nextTheme = if (currentTheme == "light") "dark" else "light"
                    settingsViewModel.changeTheme(nextTheme)
                },
                databaseDriverFactory = databaseDriverFactory
            )
        }
    }
}
