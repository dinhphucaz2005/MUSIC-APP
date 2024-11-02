package com.example.musicapp.di

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.callback.ResultCallback
import com.example.musicapp.domain.model.PlayList
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.repository.EditSongRepository
import com.example.musicapp.domain.repository.PlayListRepository
import com.example.musicapp.domain.repository.UploadRepository
import com.example.musicapp.viewmodels.PlayListDetailViewModel
import com.example.musicapp.viewmodels.MainViewModel
import com.example.musicapp.viewmodels.EditViewModel
import com.example.musicapp.viewmodels.PlayListViewModel
import com.example.musicapp.viewmodels.SelectSongViewModel
import com.example.musicapp.util.MediaControllerManager
import kotlinx.coroutines.flow.StateFlow

object FakeModule {

    val playlist = PlayList(
        id = 0,
        name = "UNNAMED"
    )

    private val editSongRepository = object : EditSongRepository {
        override suspend fun saveSongFile(
            fileName: String,
            title: String?,
            artist: String?,
            imageUri: Uri?,
            song: Song,
            onSaveFile: ResultCallback<String>
        ) {
            TODO("Not yet implemented")
        }
    }

    @Composable
    fun mediaControllerManager() = MediaControllerManager(LocalContext.current, playListRepository)

    private val playListRepository = object : PlayListRepository {
        override suspend fun reload() {
            TODO("Not yet implemented")
        }

        override suspend fun addPlaylist(name: String) {
            TODO("Not yet implemented")
        }

        override fun localFiles(): StateFlow<List<Song>> {
            TODO("Not yet implemented")
        }

        override fun savedPlayList(): StateFlow<List<PlayList>> {
            TODO("Not yet implemented")
        }

        override fun getAllSongsByPlayListId(id: Long): List<Song> {
            TODO("Not yet implemented")
        }

        override suspend fun deleteSongsFromPlayList(songsId: List<Long>, playlistId: Long) {
            TODO("Not yet implemented")
        }

        override suspend fun deletePlaylist(id: Long) {
            TODO("Not yet implemented")
        }

        override suspend fun updatePlayList(id: Long, name: String?, songs: List<Song>?) {
            TODO("Not yet implemented")
        }

    }

    private val uploadRepository = object : UploadRepository {

        override suspend fun upload(song: Song) {
            TODO("Not yet implemented")
        }
    }

    @Composable
    @UnstableApi
    fun provideViewModel() =
        MainViewModel(playListRepository, mediaControllerManager(), uploadRepository)

    @Composable
    @OptIn(UnstableApi::class)
    fun provideEditViewModel() = EditViewModel(editSongRepository, playListRepository)

    @Composable
    @UnstableApi
    fun providePlaylistViewModel() = PlayListViewModel(playListRepository)

    fun provideSelectSongViewModel() = SelectSongViewModel(playListRepository)

    @Composable
    fun providePlaylistDetailViewModel(): PlayListDetailViewModel = PlayListDetailViewModel(
        playListRepository,
        mediaControllerManager = mediaControllerManager()
    )

}