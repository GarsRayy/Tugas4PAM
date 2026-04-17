package org.garis.pam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.garis.pam.data.Article
import org.garis.pam.data.NewsRepository

// Sealed class untuk mengelola UI State dengan aman
sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    // State khusus untuk indikator Pull-to-Refresh
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    // Tambahkan ini untuk menyimpan artikel yang sedang dilihat detailnya
    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            repository.getTopHeadlines()
                .onSuccess { articles ->
                    // NewsAPI kadang mengembalikan artikel yang dihapus dengan judul "[Removed]"
                    val validArticles = articles.filter { it.title != "[Removed]" }
                    _uiState.value = NewsUiState.Success(validArticles)
                }
                .onFailure { error ->
                    _uiState.value = NewsUiState.Error(error.message ?: "Terjadi kesalahan")
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.getTopHeadlines()
                .onSuccess { articles ->
                    val validArticles = articles.filter { it.title != "[Removed]" }
                    _uiState.value = NewsUiState.Success(validArticles)
                    _isRefreshing.value = false
                }
                .onFailure { error ->
                    _uiState.value = NewsUiState.Error(error.message ?: "Gagal memuat ulang")
                    _isRefreshing.value = false
                }
        }
    }
}