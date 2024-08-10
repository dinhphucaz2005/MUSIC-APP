package com.example.mymusicapp.callback

interface ResultCallback<T> {
    fun onSuccess(result: T)
    fun onFailure(exception: Exception)
}
