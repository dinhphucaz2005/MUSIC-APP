package com.example.musicapp.data.api

import com.example.musicapp.data.api.dto.ResponseDTO
import com.example.musicapp.data.api.dto.SongDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/songs/get")
    fun getSongs(
        @Query("title") title: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<ResponseDTO<List<SongDTO>>>
}