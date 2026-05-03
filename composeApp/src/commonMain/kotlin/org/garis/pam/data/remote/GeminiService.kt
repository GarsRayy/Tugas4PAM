package org.garis.pam.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.garis.pam.core.network.NetworkResult
import org.garis.pam.core.network.runCatchingNetwork
import org.garis.pam.data.model.remote.GeminiRequest
import org.garis.pam.data.model.remote.GeminiResponse

class GeminiService(
    private val client: HttpClient,
    private val apiKey: String
) {
    companion object {
        // Menggunakan gemini-2.5-flash sesuai permintaan
        private const val MODEL_NAME = "gemini-2.5-flash"
        private const val BASE_URL =
            "https://generativelanguage.googleapis.com/v1beta/models"
    }

    suspend fun generateContent(request: GeminiRequest): NetworkResult<GeminiResponse> {
        return runCatchingNetwork {
            // Gunakan full URL string langsung untuk menghindari Ktor encoding ':' menjadi '%3A'
            val url = "$BASE_URL/$MODEL_NAME:generateContent?key=$apiKey"
            client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        }
    }
}
