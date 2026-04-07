package org.garis.pam

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import org.garis.pam.navigation.AppNavigation
import org.garis.pam.viewmodel.NoteViewModel
import org.garis.pam.viewmodel.ProfileViewModel

@Composable
fun App() {
    val profileViewModel: ProfileViewModel = viewModel { ProfileViewModel() }
    val noteViewModel: NoteViewModel       = viewModel { NoteViewModel() }

    val profileUiState by profileViewModel.uiState.collectAsState()

    val glassColors    = if (profileUiState.isDarkMode) DarkGlassColors else LightGlassColors
    val materialScheme = if (profileUiState.isDarkMode) darkColorScheme() else lightColorScheme()

    CompositionLocalProvider(LocalGlassColors provides glassColors) {
        MaterialTheme(colorScheme = materialScheme) {
            AppNavigation(
                profileViewModel = profileViewModel,
                noteViewModel    = noteViewModel,
                isDarkMode       = profileUiState.isDarkMode,
                onToggleDark     = profileViewModel::toggleDarkMode
            )
        }
    }
}