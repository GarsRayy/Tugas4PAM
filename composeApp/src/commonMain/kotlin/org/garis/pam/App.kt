package org.garis.pam

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import org.garis.pam.ui.EditProfileScreen
import org.garis.pam.ui.ProfileScreen
import org.garis.pam.viewmodel.ProfileViewModel

@Composable
fun App() {
    val viewModel: ProfileViewModel = viewModel { ProfileViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    // Pilih color set berdasarkan isDarkMode
    val glassColors = if (uiState.isDarkMode) DarkGlassColors else LightGlassColors
    val materialScheme = if (uiState.isDarkMode) darkColorScheme() else lightColorScheme()

    // Provide GlassColors ke seluruh composable tree
    CompositionLocalProvider(LocalGlassColors provides glassColors) {
        MaterialTheme(colorScheme = materialScheme) {
            if (uiState.isEditing) {
                EditProfileScreen(
                    editName     = uiState.editName,
                    editBio      = uiState.editBio,
                    onNameChange = viewModel::onNameChange,
                    onBioChange  = viewModel::onBioChange,
                    onSave       = viewModel::saveProfile,
                    onCancel     = viewModel::cancelEditing
                )
            } else {
                ProfileScreen(
                    profile      = uiState.profile,
                    isDarkMode   = uiState.isDarkMode,
                    onEditClick  = viewModel::startEditing,
                    onToggleDark = viewModel::toggleDarkMode
                )
            }
        }
    }
}