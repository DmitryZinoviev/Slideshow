package com.da.data.di

import com.da.data.downloader.DownloadWorkerImpl
import com.da.data.downloader.PlaylistDownloader
import com.da.data.local.db.AppDatabase
import com.da.data.local.db.DatabaseProvider
import com.da.data.local.db.dao.DownloadDao
import com.da.data.local.db.dao.ScreenDao
import com.da.data.local.storage.FileStorage
import com.da.data.repository.DownloadRepositoryImpl
import com.da.data.repository.PlaylistRepositoryImpl
import com.da.data.repository.UserPreferencesRepositoryImpl
import com.da.domain.download.DownloadWorker
import com.da.domain.repository.DownloadRepository
import com.da.domain.repository.PlaylistRepository
import com.da.domain.repository.UserPreferencesRepository
import org.koin.dsl.module

val dataModule = module {

    single<AppDatabase> {
        DatabaseProvider.getDatabase(get())
    }

    single<DownloadDao> {
        get<AppDatabase>().downloadDao()
    }

    single<ScreenDao> {
        get<AppDatabase>().screenDao()
    }


    single<PlaylistRepository> {

        PlaylistRepositoryImpl(get(), get(), get(),
        get(), get(), get())
    }

    single<UserPreferencesRepository> {
        UserPreferencesRepositoryImpl(
            context = get()
        )
    }

    single<DownloadRepository> {
        DownloadRepositoryImpl(get(), get())
    }


    single {
        PlaylistDownloader(get(), get())
    }

    single {
        FileStorage(get())
    }

    single<DownloadWorker> {
        DownloadWorkerImpl(get(), get())
    }






    includes(networkModule, mapperModule)

}