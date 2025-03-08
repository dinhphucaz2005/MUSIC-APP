package com.example.musicapp.other.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.extension.load
import com.example.musicapp.other.domain.repository.SongRepository
import com.example.player.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    songRepository: SongRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

//    private val _relatedPage = MutableStateFlow<RelatedPage?>(null)
//    val relatedPage: StateFlow<RelatedPage?> = _relatedPage.asStateFlow()

    val likedSongs: StateFlow<List<Song>> = songRepository.getLikedSongs()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun getRelated(mediaId: String) = load(_isLoading) {
//        TODO("Not yet implemented")
//        _relatedPage.update { null }
//
//        val relatedEndpoint = CustomYoutube
//            .next(WatchEndpoint(videoId = mediaId))
//            .getOrNull()
//            ?.relatedEndpoint
//            ?: return@load
//
//        val relatedPage = CustomYoutube
//            .related(relatedEndpoint)
//            .getOrNull()
//            ?: return@load
//
//        _relatedPage.update { relatedPage }

    }


}
