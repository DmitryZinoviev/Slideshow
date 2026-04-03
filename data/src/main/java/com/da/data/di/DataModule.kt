package com.da.data.di

import androidx.room.Room
import com.da.data.local.dao.PlaylistDao
import com.da.data.local.db.AppDatabase
import com.da.data.repository.PlaylistRepositoryImpl
import com.da.domain.repository.PlaylistRepository
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

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            playlistDao = get()
        )
    }
}