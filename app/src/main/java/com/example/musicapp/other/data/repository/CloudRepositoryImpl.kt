package com.example.musicapp.other.data.repository

import com.example.musicapp.other.data.FirebaseDataSource
import com.example.musicapp.other.domain.model.AudioSource
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.other.domain.model.ThumbnailSource
import com.example.musicapp.other.domain.repository.CloudRepository
import javax.inject.Inject

class CloudRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : CloudRepository {


    override fun upload(songs: List<Song>) = firebaseDataSource.uploadSongs(songs)

    override suspend fun load(): List<Song> {
        val serverSongs = firebaseDataSource.load()
        return serverSongs.map {
            Song(
                id = it.id,
                title = it.title,
                artist = it.artist,
                audioSource = AudioSource.FromUrl(it.songUri),
                thumbnailSource = ThumbnailSource.FromUrl(it.thumbnailUri),
                durationMillis = it.durationMillis
            )
        }
    }
}