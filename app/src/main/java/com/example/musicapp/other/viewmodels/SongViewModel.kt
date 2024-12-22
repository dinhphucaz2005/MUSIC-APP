package com.example.musicapp.other.viewmodels

import androidx.lifecycle.ViewModel
import com.example.innertube.CustomYoutube
import com.example.innertube.models.WatchEndpoint
import com.example.innertube.pages.RelatedPage
import com.example.musicapp.extension.load
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor() : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _relatedPage = MutableStateFlow<RelatedPage?>(null)
    val relatedPage: StateFlow<RelatedPage?> = _relatedPage.asStateFlow()


    fun getRelated(mediaId: String) = load(_isLoading) {

        _relatedPage.update { null }

        val relatedEndpoint = CustomYoutube
            .next(WatchEndpoint(videoId = mediaId))
            .getOrNull()
            ?.relatedEndpoint
            ?: return@load

        val relatedPage = CustomYoutube
            .related(relatedEndpoint)
            .getOrNull()
            ?: return@load

        _relatedPage.update { relatedPage }

    }
}
