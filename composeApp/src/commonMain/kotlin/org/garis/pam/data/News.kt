package org.garis.pam.data

import kotlinx.serialization.Serializable

// Wrapper utama untuk response JSON dari NewsAPI
@Serializable
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

// Data class untuk setiap item artikel
@Serializable
data class Article(
    val title: String,
    val description: String? = null,
    val urlToImage: String? = null,
    val url: String,
    val publishedAt: String
)