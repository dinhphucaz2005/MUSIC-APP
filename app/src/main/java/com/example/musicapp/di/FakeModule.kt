package com.example.musicapp.di

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.musicapp.music.data.database.entity.PlaylistEntity
import com.example.musicapp.music.data.database.entity.SongEntity
import com.example.musicapp.music.domain.model.CurrentSong
import com.example.musicapp.music.domain.model.FirebaseSong
import com.example.musicapp.music.domain.model.LocalSong
import com.example.musicapp.music.domain.model.PlayBackState
import com.example.musicapp.music.domain.model.Playlist
import com.example.musicapp.music.domain.model.Queue
import com.example.musicapp.music.domain.model.Song
import com.example.musicapp.music.domain.model.ThumbnailSource
import com.example.musicapp.music.domain.repository.CloudRepository
import com.example.musicapp.music.domain.repository.SongRepository
import com.example.musicapp.music.presentation.ui.feature.home.HomeViewModel
import com.example.musicapp.music.presentation.ui.feature.playlist.PlaylistDetailViewModel
import com.example.musicapp.music.presentation.ui.feature.playlist.PlaylistRepository
import com.example.musicapp.util.MediaControllerManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

object FakeModule {

//    val artistPage: ArtistPage = ArtistPage(
//        artist = ArtistItem(
//            id = "TODO()",
//            title = "TODO()",
//            thumbnail = "TODO()",
//            shuffleEndpoint = null,
//            radioEndpoint = null
//        ),
//        description = null
//    )

    private val localSongs = listOf(
        LocalSong(
            id = "1",
            title = "Song A",
            artist = "Artist X",
            uri = Uri.EMPTY,
            thumbnailSource = ThumbnailSource.FromUrl("https://via.placeholder.com/150"),
            durationMillis = 180000
        ),
        LocalSong(
            id = "2",
            title = "Song B",
            artist = "Artist Y",
            uri = Uri.EMPTY,
            thumbnailSource = ThumbnailSource.FromUrl("https://via.placeholder.com/150"),
            durationMillis = 200000
        ),
        LocalSong(
            id = "3",
            title = "Song C",
            artist = "Artist Z",
            uri = Uri.EMPTY,
            thumbnailSource = ThumbnailSource.FromUrl("https://via.placeholder.com/150"),
            durationMillis = 240000
        )
    )

    private val playlists = MutableStateFlow(
        listOf(
            Playlist(
                id = 1,
                name = "My Favorite Songs",
                songs = listOf(localSongs[0], localSongs[1])
            ),
            Playlist(
                id = 2,
                name = "Chill Vibes",
                songs = listOf(localSongs[2])
            )
        )
    )

    private val likedSongs = MutableStateFlow(listOf<Song>(localSongs[0]))

    private val songRepository: SongRepository = object : SongRepository {

        override suspend fun getSongsFromPlaylist(playlistId: Int): List<Song> {
            return playlists.value.find { it.id == playlistId }?.songs ?: emptyList()
        }

        override suspend fun createPlayList(name: String) {
            val newPlaylist = Playlist(
                id = (playlists.value.maxOfOrNull { it.id } ?: 0) + 1,
                name = name
            )
            playlists.value += newPlaylist
        }

        override suspend fun updatePlaylist(playlistId: Int, name: String) {
            playlists.value = playlists.value.map {
                if (it.id == playlistId) it.copy(name = name) else it
            }
        }

        override suspend fun deletePlayList(id: Int) {
            playlists.value = playlists.value.filterNot { it.id == id }
        }

        override suspend fun addSongsToPlaylist(playListId: Int, localSongs: List<Song>) {
            playlists.value = playlists.value.map {
                if (it.id == playListId) it.copy(songs = it.songs + localSongs) else it
            }
        }

        override suspend fun deleteSongs(songIds: List<String>) {
            playlists.value = playlists.value.map { playlist ->
                playlist.copy(
                    songs = playlist.songs.filterNot { it.id in songIds }
                )
            }
        }

        override suspend fun getLocalSong(): List<LocalSong> = localSongs

        override fun getPlayLists(): Flow<List<Playlist>> = playlists

        override suspend fun likeSong(song: Song) {
            likedSongs.value += song
        }

        override suspend fun unlikeSong(song: Song) {
            likedSongs.value = likedSongs.value.filterNot { it.id == song.id }
        }

        override fun getLikedSongs(): Flow<List<Song>> = likedSongs
        override suspend fun savePlaylist(
            name: String,
            description: String,
            createdBy: String,
            songs: List<SongEntity>
        ) {
            TODO("Not yet implemented")
        }

        override suspend fun getPlaylists(): List<PlaylistEntity> {
            TODO("Not yet implemented")
        }
    }

    private val cloudRepository: CloudRepository = object : CloudRepository {

        override suspend fun load(): List<FirebaseSong> {
            return listOf(
                FirebaseSong(
                    id = "cloud_1",
                    title = "Cloud Song 1",
                    artist = "Cloud Artist 1",
                    audioUrl = "https://example.com/audio1.mp3",
                    thumbnailSource = ThumbnailSource.FromUrl("https://example.com/image1.jpg"),
                    durationMillis = 210_000L
                ),
                FirebaseSong(
                    id = "cloud_2",
                    title = "Cloud Song 2",
                    artist = "Cloud Artist 2",
                    audioUrl = "https://example.com/audio2.mp3",
                    thumbnailSource = ThumbnailSource.FromUrl("https://example.com/image2.jpg"),
                    durationMillis = 185_000L
                )
            )
        }

        override fun upload(localSongs: List<LocalSong>) {
            localSongs.forEach { song ->
                println("Uploading song to cloud: ${song.title} by ${song.artist}")
            }
        }
    }

    fun provideHomeViewModel(): HomeViewModel = HomeViewModel(songRepository = songRepository)

    val mediaControllerManager =
        object : MediaControllerManager {
            override val audioSessionId: StateFlow<Int?>
                get() = MutableStateFlow(0)
            override val playBackState: StateFlow<PlayBackState>
                get() = MutableStateFlow(PlayBackState())
            override val currentSong: StateFlow<CurrentSong>
                get() = MutableStateFlow(
                    CurrentSong(
                        data = localSongs.random(),
                        isLiked = false
                    )
                )
            override val queue: StateFlow<Queue?>
                get() = MutableStateFlow(null)

            override fun playQueue(songs: List<Song>, index: Int, id: String): Unit? = null

            override fun computePlaybackFraction(): Float? = null

            override fun getCurrentTrackPosition(): Long? = null

            override fun downLoadCurrentSong() {}

            override fun seekToSliderPosition(sliderPosition: Float) {}

            override fun updatePlayListState() {}

            override fun playPreviousSong(): Unit? = null

            override fun togglePlayPause(): Unit? = null

            override fun playNextSong(): Unit? = null

            override fun getCurrentMediaIndex(): Int? = null

            override fun playAtIndex(index: Int): Unit? = null

            override fun addToNext(song: Song) {}

            override fun addToQueue(song: Song) {}

            override fun dispose() {}

            override fun toggleLikedCurrentSong() {}

        }

    val playlist = Playlist(
        id = Random.nextInt(),
        name = "Nhạc Mix Gây Nghiện 2019",
        songs = List(20) { Song.unidentifiedSong() }
    )

    val localSong = localSongs.random()

    fun providePlaylistDetailViewModel(): PlaylistDetailViewModel {
        @SuppressLint("StaticFieldLeak")
        return object : PlaylistDetailViewModel(
            PlaylistRepository(), SavedStateHandle(mapOf("playlistId" to "1"))
        ) {
        }
    }
}

@Composable
inline fun <reified T : ViewModel> fakeViewModel(): T {
    val isPreview = LocalInspectionMode.current
    return if (isPreview) {
        when (T::class) {
            HomeViewModel::class -> FakeModule.provideHomeViewModel()
            PlaylistDetailViewModel::class -> FakeModule.providePlaylistDetailViewModel()
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    } else hiltViewModel<T>()
}
