package org.garis.pam.data.repository

import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.garis.pam.data.model.Article
import org.garis.pam.data.model.NewsResponse

class NewsRepository(private val client: HttpClient) {
    private val apiKey = "96cd83ec7f144282a5822d11e2ab5fad"
    private val baseUrl = "https://newsapi.org/v2"
    
    // Inisialisasi local storage
    private val settings = Settings()
    private val CACHE_KEY = "offline_news_cache"

    suspend fun getTopHeadlines(): Result<List<Article>> {
        return try {
            val response: NewsResponse = client.get("$baseUrl/top-headlines") {
                url {
                    // Parameter untuk request berita
                    parameters.append("country", "us")
                    parameters.append("category", "general")
                    parameters.append("apiKey", apiKey)
                    // Menambahkan timestamp untuk menghindari caching agar berita selalu fresh dari server
                    parameters.append("t", kotlinx.datetime.Clock.System.now().toEpochMilliseconds().toString())
                }
            }.body()
            
            val articles = response.articles
            
            // 1. Jika sukses internetan, simpan data terbaru ke Cache Lokal
            // Kita ubah List<Article> menjadi format String JSON
            val jsonString = Json.encodeToString(articles)
            settings.putString(CACHE_KEY, jsonString)
            
            Result.success(articles)
            
        } catch (e: Exception) {
            // 2. Jika GAGAL internetan (Offline/Error), coba ambil dari Cache
            val cachedJson = settings.getStringOrNull(CACHE_KEY)
            
            if (cachedJson != null) {
                try {
                    // Ubah kembali String JSON menjadi List<Article>
                    val cachedArticles = Json.decodeFromString<List<Article>>(cachedJson)
                    Result.success(cachedArticles)
                } catch (decodeEx: Exception) {
                    Result.failure(e) // Gagal membaca cache
                }
            } else {
                // Tidak ada internet dan cache masih kosong
                Result.failure(Exception("Tidak ada koneksi internet dan belum ada data tersimpan."))
            }
        }
    }
}
