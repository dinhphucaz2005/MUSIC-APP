package com.example.musicapp

import android.app.Application
import android.content.Context
import androidx.room.Room.inMemoryDatabaseBuilder
import com.example.musicapp.data.database.AppDAO
import com.example.musicapp.data.database.AppDatabase
import com.example.musicapp.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestAppDatabaseModule {

    @Provides
    @Singleton
    fun provideInMemoryDb(@ApplicationContext context: Context): AppDatabase {
        return inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideYourDao(database: AppDatabase): AppDAO {
        return database.appDAO()
    }
}
