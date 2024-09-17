package com.example.musicapp.callback

sealed class AppResource<out T : Any> {
    data class Success<out T : Any>(val data: T?) : AppResource<T>()
    data class Error(val error: Exception) : AppResource<Nothing>()
}