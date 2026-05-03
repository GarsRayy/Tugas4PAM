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
    suspend fun generateContent(request: GeminiRequest): NetworkResult<GeminiResponse> {
        return runCatchingNetwork {
            client.post {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "generativelanguage.googleapis.com"
                    path("v1beta/models/gemini-2.5-flash:generateContent")
                    parameters.append("key", apiKey)
                }
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        }
    }
}
