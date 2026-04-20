package org.garis.pam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.garis.pam.data.SettingsManager

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
}