package org.garis.pam.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.garis.pam.data.UserProfile
import org.garis.pam.data.myProfile

// ── Data class UI State (sesuai materi hal 25) ──
data class ProfileUiState(
    val profile: UserProfile = myProfile,
    val isDarkMode: Boolean  = true,      // Dark mode default ON
    val isEditing: Boolean   = false,     // Apakah sedang di mode edit
    // Field sementara saat edit (state hoisting dari form)
    val editName: String     = myProfile.name,
    val editBio: String      = myProfile.bio
)

// ── ViewModel (sesuai materi hal 24) ──
class ProfileViewModel : ViewModel() {

    // Private mutable state — hanya ViewModel yang bisa ubah
    private val _uiState = MutableStateFlow(ProfileUiState())

    // Public read-only state — yang dibaca oleh UI
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // ── Event: toggle dark/light mode (BONUS +10%) ──
    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    // ── Event: buka form edit ──
    fun startEditing() {
        _uiState.update { currentState ->
            currentState.copy(
                isEditing = true,
                editName  = currentState.profile.name,
                editBio   = currentState.profile.bio
            )
        }
    }

    // ── Event: update field nama saat diketik (state hoisting) ──
    fun onNameChange(newName: String) {
        _uiState.update { it.copy(editName = newName) }
    }

    // ── Event: update field bio saat diketik (state hoisting) ──
    fun onBioChange(newBio: String) {
        _uiState.update { it.copy(editBio = newBio) }
    }

    // ── Event: simpan perubahan ──
    fun saveProfile() {
        _uiState.update { currentState ->
            currentState.copy(
                profile = currentState.profile.copy(
                    name = currentState.editName.ifBlank { currentState.profile.name },
                    bio  = currentState.editBio.ifBlank  { currentState.profile.bio }
                ),
                isEditing = false
            )
        }
    }

    // ── Event: batal edit ──
    fun cancelEditing() {
        _uiState.update { it.copy(isEditing = false) }
    }
}