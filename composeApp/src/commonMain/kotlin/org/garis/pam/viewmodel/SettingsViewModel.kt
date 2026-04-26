package org.garis.pam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.garis.pam.data.local.SettingsManager

class SettingsViewModel(private val settingsManager: SettingsManager) : ViewModel() {

    // Konversi Flow dari SettingsManager menjadi StateFlow untuk UI
    val currentTheme: StateFlow<String> = settingsManager.themeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "aurora_glass" // Default tema
        )

    val currentSortOrder: StateFlow<String> = settingsManager.sortOrderFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "newest"
        )

    val isBiometricEnabled: StateFlow<Boolean> = settingsManager.biometricEnabledFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val hiddenPassword: StateFlow<String> = settingsManager.hiddenPasswordFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    fun changeTheme(theme: String) {
        viewModelScope.launch {
            settingsManager.setTheme(theme)
        }
    }

    fun changeSortOrder(order: String) {
        viewModelScope.launch {
            settingsManager.setSortOrder(order)
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setBiometricEnabled(enabled)
        }
    }

    fun setHiddenPassword(password: String) {
        viewModelScope.launch {
            settingsManager.setHiddenPassword(password)
        }
    }
}
