package com.da.data.di

import androidx.room.Room
import com.da.data.local.db.dao.PlaylistDao
import com.da.data.local.db.AppDatabase
import com.da.data.local.db.dao.ScreenDao
import com.da.data.repository.PlaylistRepositoryImpl
import com.da.data.repository.UserPreferencesRepositoryImpl
import com.da.domain.repository.PlaylistRepository
import com.da.domain.repository.UserPreferencesRepository
import org.koin.dsl.module

val dataModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "app_db"
        ).build()
    }

    single<PlaylistDao> {
        get<AppDatabase>().playlistDao()
    }

    single<ScreenDao> {
        get<AppDatabase>().screenDao()
    }


    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get(), get())
    }

    single<UserPreferencesRepository> {
        UserPreferencesRepositoryImpl(
            context = get()
        )
    }


    includes(networkModule, mapperModule)

}