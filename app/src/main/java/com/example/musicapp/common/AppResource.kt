package com.example.musicapp.common


sealed class AppResource<out T : Any> {
    data class Success<out T : Any>(val data: T?) : AppResource<T>()
    data class Error(val error: String) : AppResource<Nothing>()
}