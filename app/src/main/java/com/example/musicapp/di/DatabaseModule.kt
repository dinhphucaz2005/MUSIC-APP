package com.example.musicapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.musicapp.constants.DATABASE_NAME
import com.example.musicapp.other.data.database.AppDAO
import com.example.musicapp.other.data.database.AppDatabase
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
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAppDao(database: AppDatabase): AppDAO = database.appDAO()
}
