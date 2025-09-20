package nd.phuc.core.database.di

import androidx.room.Room
import nd.phuc.core.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    single { get<AppDatabase>().songDao() }
    single { get<AppDatabase>().playlistDao() }
}
