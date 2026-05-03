package org.garis.pam.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class AiInsightResponse(
    val title: String,
    val insights: List<String>,
    val actions: List<AiAction>,
    val suggestedTheme: AiTheme? = null
)

@Serializable
data class AiTheme(
    val primaryHex: String,
    val secondaryHex: String,
    val accentHex: String,
    val backgroundHex: String,
    val name: String
)

@Serializable
data class AiAction(
    val label: String,
    val type: String, // e.g., "TODO", "CALENDAR", "SEARCH"
    val value: String
)
