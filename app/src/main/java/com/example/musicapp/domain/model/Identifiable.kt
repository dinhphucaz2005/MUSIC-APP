package com.example.musicapp.domain.model

import androidx.compose.runtime.Stable

@Stable
interface Identifiable {
    val id: Any
}