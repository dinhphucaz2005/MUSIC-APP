package com.example.mymusicapp.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.AppViewModel
import com.example.mymusicapp.domain.model.Song
import com.example.mymusicapp.domain.repository.SongFileRepository
import com.example.mymusicapp.util.MediaControllerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object FakeModule {
    fun provideSongFileRepository(): SongFileRepository {
        return object : SongFileRepository {
            override suspend fun getLocal(): StateFlow<List<Song>> {
                return MutableStateFlow(emptyList())
            }

            override suspend fun search(searchQuery: String): List<Song>? {
                TODO("Not yet implemented")
            }

            override fun reload() {

            }

        }
    }

    @Composable
    fun provideMediaControllerManager(): MediaControllerManager {
        return MediaControllerManager(LocalContext.current)
    }

    @Composable
    @UnstableApi
    fun provideViewModel(): AppViewModel {
        return AppViewModel(provideSongFileRepository(), provideMediaControllerManager())
    }
}