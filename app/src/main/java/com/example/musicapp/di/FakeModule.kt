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
import com.example.musicapp.util.MediaControllerManager
import com.example.musicapp.viewmodels.EditPlayListViewModel
import com.example.musicapp.viewmodels.EditViewModel
import com.example.musicapp.viewmodels.MainViewModel
import com.example.musicapp.viewmodels.PlayListDetailViewModel
import com.example.musicapp.viewmodels.PlayListViewModel
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

        override fun getLocalPlayList(): StateFlow<PlayList> {
            TODO("Not yet implemented")
        }

        override fun getSavedPlayLists(): StateFlow<List<PlayList>> {
            TODO("Not yet implemented")
        }

        override fun getPlayList(playListId: Long): PlayList? {
            TODO("Not yet implemented")
        }

        override suspend fun createPlayList(name: String) {
            TODO("Not yet implemented")
        }

        override suspend fun savePlayList(id: Long, name: String) {
            TODO("Not yet implemented")
        }

        override suspend fun deletePlayList(id: Long) {
            TODO("Not yet implemented")
        }

        override suspend fun addSongs(playListId: Long, selectedSongIds: List<Long>) {
            TODO("Not yet implemented")
        }

        override suspend fun deleteSongs(playListId: Long, selectedSongIds: List<Long>) {
            TODO("Not yet implemented")
        }
    }

    @Composable
    fun provideMainViewModel() =
        MainViewModel(mediaControllerManager(), playListRepository)

    @Composable
    @OptIn(UnstableApi::class)
    fun provideEditViewModel() =
        EditViewModel(editSongRepository, playListRepository)

    @Composable
    @UnstableApi
    fun providePlaylistViewModel() = PlayListViewModel(playListRepository)

    fun provideSelectSongViewModel() = EditPlayListViewModel(playListRepository)

    @Composable
    fun providePlaylistDetailViewModel(): PlayListDetailViewModel = PlayListDetailViewModel(
        playListRepository,
        mediaControllerManager = mediaControllerManager()
    )

}