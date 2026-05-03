package org.garis.pam.core.network

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(
        val code: Int? = null,
        val message: String? = null,
        val exception: Throwable? = null
    ) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

inline fun <T> runCatchingNetwork(block: () -> T): NetworkResult<T> {
    return try {
        NetworkResult.Success(block())
    } catch (e: Exception) {
        // Logika retry atau pemetaan error Ktor bisa ditambahkan di sini
        NetworkResult.Error(exception = e, message = e.message ?: "Unknown Error")
    }
}
