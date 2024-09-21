package com.example.musicapp.data.repository

import android.net.Uri
import com.example.musicapp.common.AppResource
import com.example.musicapp.data.api.ApiService
import com.example.musicapp.data.api.dto.ResponseDTO
import com.example.musicapp.data.api.dto.SongDTO
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.CloudRepository
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CloudRepositoryImpl(
    private val apiService: ApiService
) : CloudRepository {
    override suspend fun loadSongs(title: String, page: Int, size: Int): AppResource<List<Song>> {
        return suspendCoroutine { continuation ->
            apiService.getSongs("", page, size)
                .enqueue(object : Callback<ResponseDTO<List<SongDTO>>> {
                    override fun onResponse(
                        p0: Call<ResponseDTO<List<SongDTO>>>,
                        p1: Response<ResponseDTO<List<SongDTO>>>
                    ) {
                        if (p1.isSuccessful) {
                            val response = p1.body()
                            if (response != null) {
                                val songs = response.data?.map { songDTO ->
                                    Song(
                                        fileName = songDTO.title,
                                        title = songDTO.title,
                                        author = songDTO.author,
                                        path = songDTO.url
                                    )
                                }
                                continuation.resume(AppResource.Success(songs ?: emptyList()))
                            } else {
                                continuation.resume(AppResource.Error("Songs not found"))
                            }
                        } else {
                            val json = JSONObject(p1.errorBody()?.string() ?: "{}")
                            val errorMessage = json.optString("msg")
                            continuation.resume(AppResource.Error("Failed: $errorMessage"))
                        }
                    }

                    override fun onFailure(
                        p0: Call<ResponseDTO<List<SongDTO>>>,
                        p1: Throwable
                    ) {
                        continuation.resume(AppResource.Error("${p1.message}"))
                    }
                })
        }
    }
}