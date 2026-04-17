package org.garis.pam.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class NewsRepository(private val client: HttpClient) {
    // TODO: Masukkan API Key dari newsapi.org
    private val apiKey = "96cd83ec7f144282a5822d11e2ab5fad"
    private val baseUrl = "https://newsapi.org/v2"

    suspend fun getTopHeadlines(): Result<List<Article>> {
        return try {
            val response: NewsResponse = client.get("$baseUrl/top-headlines") {
                url {
                    // Parameter untuk request berita (contoh: negara US, kategori teknologi)
                    parameters.append("country", "us")
                    parameters.append("category", "general")
                    parameters.append("apiKey", apiKey)
                }
            }.body()
            Result.success(response.articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}