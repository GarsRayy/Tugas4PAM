package org.garis.pam.data.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalSettingsApi::class)
class SettingsManager(settings: Settings = Settings()) {
    // Mengubah settings biasa menjadi Flow agar reaktif
    private val flowSettings: FlowSettings = (settings as ObservableSettings).toFlowSettings()

    companion object {
        private const val KEY_THEME = "app_theme"
        private const val KEY_SORT_ORDER = "note_sort_order"
        private const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
        private const val KEY_HIDDEN_PASSWORD = "hidden_password"
    }

    // Mengambil data sebagai Flow
    val themeFlow: Flow<String> = flowSettings.getStringFlow(KEY_THEME, "aurora_glass")
    val sortOrderFlow: Flow<String> = flowSettings.getStringFlow(KEY_SORT_ORDER, "newest")
    val biometricEnabledFlow: Flow<Boolean> = flowSettings.getBooleanFlow(KEY_BIOMETRIC_ENABLED, false)
    val hiddenPasswordFlow: Flow<String> = flowSettings.getStringFlow(KEY_HIDDEN_PASSWORD, "")

    // Fungsi suspend untuk menyimpan data baru
    suspend fun setTheme(theme: String) {
        flowSettings.putString(KEY_THEME, theme)
    }

    suspend fun setSortOrder(order: String) {
        flowSettings.putString(KEY_SORT_ORDER, order)
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        flowSettings.putBoolean(KEY_BIOMETRIC_ENABLED, enabled)
    }

    suspend fun setHiddenPassword(password: String) {
        flowSettings.putString(KEY_HIDDEN_PASSWORD, password)
    }
}
