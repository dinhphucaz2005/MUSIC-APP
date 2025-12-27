package nd.phuc.musicapp.ui.songs

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nd.phuc.musicapp.data.repository.SongRepository
import nd.phuc.musicapp.data.repository.SongRepositoryState

class SongsViewModel(private val repository: SongRepository) : ViewModel() {
    val state: StateFlow<SongRepositoryState> = repository.state

    fun loadSongs(context: Context) {
        viewModelScope.launch {
            repository.loadSongs(context)
        }
    }
}
