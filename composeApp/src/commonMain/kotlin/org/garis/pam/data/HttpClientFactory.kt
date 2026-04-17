package org.garis.pam.data

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true // Sangat penting agar aplikasi tidak crash
                })
            }
            install(Logging) {
                level = LogLevel.BODY // Menampilkan log request/response di Logcat
            }
        }
    }
}