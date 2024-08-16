package com.example.musicapp.di

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.R
import com.example.musicapp.ui.AppViewModel
import com.example.musicapp.callback.ResultCallback
import com.example.musicapp.domain.model.Playlist
import com.example.musicapp.domain.model.Song
import com.example.musicapp.domain.model.UserSetting
import com.example.musicapp.domain.repository.EditSongRepository
import com.example.musicapp.domain.repository.PlaylistRepository
import com.example.musicapp.domain.repository.SongFileRepository
import com.example.musicapp.domain.repository.UserRepository
import com.example.musicapp.ui.screen.edit.EditViewModel
import com.example.musicapp.ui.screen.playlist.PlaylistViewModel
import com.example.musicapp.util.MediaControllerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object FakeModule {

    private fun provideSongFileRepository(): SongFileRepository {
        return object : SongFileRepository {
            override suspend fun getLocal(): StateFlow<List<Song>> =
                MutableStateFlow(List(20) {
                    Song(
                        fileName = "NO SONG FOUND",
                        smallBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                            .asImageBitmap()
                    )
                })

            override suspend fun search(searchQuery: String): List<Song>? = null
            override fun reload() {}
        }
    }

    private fun provideEditSongRepository(): EditSongRepository {
        return object : EditSongRepository {
            override suspend fun saveSongFile(
                fileName: String,
                title: String?,
                artist: String?,
                imageUri: Uri?,
                song: Song,
                onSaveFile: ResultCallback<String>
            ) {

            }
        }
    }

    @Composable
    fun provideMediaControllerManager(): MediaControllerManager {
        return MediaControllerManager(LocalContext.current)
    }

    private fun provideUserRepository(): UserRepository {
        return object : UserRepository {
            override suspend fun getUserSetting(): UserSetting {
                return UserSetting()
            }

            override suspend fun saveUserSetting(userSetting: UserSetting) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun providePlaylistRepository(): PlaylistRepository {
        val playlistRepository = object : PlaylistRepository {
            override fun getPlaylist(): StateFlow<List<Playlist>> {
//                return MutableStateFlow(List(20) { Playlist() })
                return MutableStateFlow(emptyList())
            }

            override suspend fun addPlaylist(
                playlist: Playlist,
                onResult: ResultCallback<String>?
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun removePlaylist(
                playlist: Playlist,
                onResult: ResultCallback<String>?
            ) {
                TODO("Not yet implemented")
            }

            override suspend fun updatePlaylist(
                playlist: Playlist,
                onResult: ResultCallback<String>?
            ) {
                TODO("Not yet implemented")
            }
        }
        return playlistRepository
    }


    @Composable
    @UnstableApi
    fun provideViewModel(): AppViewModel {
        return AppViewModel(
            provideSongFileRepository(),
            provideMediaControllerManager()
        )
    }

    @Composable
    @OptIn(UnstableApi::class)
    fun provideEditViewModel(): EditViewModel {
        return EditViewModel(provideEditSongRepository(), provideSongFileRepository())
    }

    @UnstableApi
    fun providePlaylistViewModel(): PlaylistViewModel {
        return PlaylistViewModel(providePlaylistRepository())
    }

}