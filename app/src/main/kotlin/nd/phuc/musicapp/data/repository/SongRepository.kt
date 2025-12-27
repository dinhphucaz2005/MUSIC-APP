package nd.phuc.musicapp.data.repository

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import nd.phuc.musicapp.model.LocalSong

data class SongRepositoryState(
    val songs: List<LocalSong> = emptyList(),
    val isLoading: Boolean = false,
)

class SongRepository {
    private val _state = MutableStateFlow(SongRepositoryState())
    val state: StateFlow<SongRepositoryState> = _state.asStateFlow()

    fun loadSongs(context: Context) {
        _state.value = _state.value.copy(isLoading = true)
        LocalSongExtractor.initialize(context)
        LocalSongExtractor.extracts(context) { result ->
            when (result) {
                is ExtractLocalSongResult.Success -> {
                    val currentSongs = _state.value.songs
                    _state.value = _state.value.copy(
                        songs = currentSongs + result.song
                    )
                }

                is ExtractLocalSongResult.Finished -> {
                    _state.value = _state.value.copy(isLoading = false)
                }

                else -> {}
            }
        }
    }
}
