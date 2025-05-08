package com.example.musicapp.music.data.repository

import com.example.musicapp.music.data.FirebaseDataSource
import com.example.musicapp.music.domain.model.FirebaseSong
import com.example.musicapp.music.domain.model.LocalSong
import com.example.musicapp.music.domain.model.ThumbnailSource
import com.example.musicapp.music.domain.repository.CloudRepository
import javax.inject.Inject

class CloudRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : CloudRepository {


    override suspend fun load(): List<FirebaseSong> {
        val serverSongs = firebaseDataSource.load()
        return serverSongs.map {
            FirebaseSong(
                id = it.id,
                title = it.title,
                artist = it.artist,
                audioUrl = it.songUri,
                thumbnailSource = ThumbnailSource.FromUrl(it.thumbnailUri),
                durationMillis = it.durationMillis
            )
        }
    }

    override fun upload(localSongs: List<LocalSong>) {
        firebaseDataSource.uploadSongs(localSongs)
    }
}