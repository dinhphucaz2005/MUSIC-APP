package com.example.musicapp.di

import android.content.Context
import androidx.room.Room
import com.example.musicapp.common.AppCommon
import com.example.musicapp.data.database.AppDAO
import com.example.musicapp.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppCommon.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAppDao(database: AppDatabase): AppDAO = database.appDAO()
}