package com.example.musicapp.di

import com.example.musicapp.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://192.168.0.102:8888"

    private val gsonConverterFactory by lazy { GsonConverterFactory.create() }

    @Provides
    @Singleton
    fun provideApiServiceWithoutToken(): ApiService {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory).build()
            .create(ApiService::class.java)
    }
}