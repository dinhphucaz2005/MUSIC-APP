package com.example.musicapp.data.api.dto

import com.google.gson.annotations.SerializedName

class ResponseDTO<out T>(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T? = null
)