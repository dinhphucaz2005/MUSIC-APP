package com.example.musicapp.callback

interface ResultCallback<T> {
    fun onSuccess(result: T)
    fun onFailure(exception: Exception)
}