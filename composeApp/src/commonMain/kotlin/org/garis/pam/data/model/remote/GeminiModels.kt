package org.garis.pam.data.model.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GeminiRequest(
    val contents: List<Content>,
    @SerialName("generation_config") val generationConfig: GenerationConfig? = null,
    @SerialName("system_instruction") val systemInstruction: Content? = null
)

@Serializable
data class Content(
    val role: String? = null,
    val parts: List<Part>
)

@Serializable
data class Part(
    val text: String? = null,
    val inlineData: InlineData? = null
)

@Serializable
data class InlineData(
    @SerialName("mime_type") val mimeType: String,
    val data: String
)

@Serializable
data class GenerationConfig(
    val temperature: Float? = 0.7f,
    val topK: Int? = 40,
    val topP: Float? = 0.95f,
    val maxOutputTokens: Int? = 2048,
    @SerialName("response_mime_type") val responseMimeType: String? = "application/json"
)

@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null,
    @SerialName("usage_metadata") val usageMetadata: UsageMetadata? = null,
    val error: GeminiError? = null
)

@Serializable
data class GeminiError(
    val code: Int? = null,
    val message: String? = null,
    val status: String? = null
)

@Serializable
data class Candidate(
    val content: Content,
    @SerialName("finish_reason") val finishReason: String? = null
)

@Serializable
data class UsageMetadata(
    @SerialName("total_token_count") val totalTokenCount: Int
)
