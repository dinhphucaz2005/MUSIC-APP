package nd.phuc.musicapp.other.viewmodels

//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import nd.phuc.musicapp.constants.DEFAULT_DURATION_MILLIS
//import nd.phuc.musicapp.extension.load
//import nd.phuc.musicapp.other.domain.model.FirebaseSong
//import nd.phuc.musicapp.other.domain.repository.CloudRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.onStart
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.flow.update
//import javax.inject.Inject
//
//@HiltViewModel
//class CloudViewModel @Inject constructor(
//    private val cloudRepository: CloudRepository,
//) : ViewModel() {
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading = _isLoading
//        .onStart { if (_songs.value.isEmpty()) loadSong() }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(DEFAULT_DURATION_MILLIS), false)
//
//    private val _songs = MutableStateFlow<List<FirebaseSong>>(emptyList())
//    val songs: StateFlow<List<FirebaseSong>> = _songs.asStateFlow()
//
//    private fun loadSong() = load(_isLoading) {
//        _songs.update { cloudRepository.load() }
//    }
//
//
//}
