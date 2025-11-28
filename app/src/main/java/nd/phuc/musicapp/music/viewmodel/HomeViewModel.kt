package nd.phuc.musicapp.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nd.phuc.core.domain.model.LocalSong
import nd.phuc.core.domain.repository.abstraction.LocalSongRepository
import timber.log.Timber

class HomeViewModel(
    private val songRepository: LocalSongRepository,
) : ViewModel() {

    init {
        viewModelScope.launch {
            songRepository.getSongs()
        }
    }

    val songs: StateFlow<List<LocalSong>> =
        songRepository.allSongs
            .onEach { songs -> Timber.Forest.d("Songs: ${songs.size}") }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val likedSongs: StateFlow<Set<LocalSong>> =
        songRepository.allSongs
            .map { songs -> songs.filter { it.isLiked }.toSet() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(5000),
                initialValue = emptySet()
            )
}