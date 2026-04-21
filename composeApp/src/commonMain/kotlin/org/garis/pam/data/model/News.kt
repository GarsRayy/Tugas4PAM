package org.garis.pam.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

@Serializable
data class Article(
    val title: String,
    val description: String? = null,
    val urlToImage: String? = null,
    val url: String,
    val publishedAt: String
)
