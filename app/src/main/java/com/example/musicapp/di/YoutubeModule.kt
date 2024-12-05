package com.example.musicapp.di

import com.example.musicapp.youtube.data.repository.DefaultYoutubeRepository
import com.example.musicapp.youtube.domain.YoutubeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object YoutubeModule {

    @Provides
    @Singleton
    fun provideYoutubeRepository(): YoutubeRepository = DefaultYoutubeRepository()

}