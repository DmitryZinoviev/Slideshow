package com.da.domain.di

import com.da.domain.diff.ScreenDiffCalculator
import com.da.domain.useCases.CheckPendingDownloadUseCase
import com.da.domain.useCases.CleanTempFilesUseCase
import com.da.domain.useCases.GetPlaylistForReplayUseCase
import com.da.domain.useCases.GetScreenKeyUseCase
import com.da.domain.useCases.SaveScreenKeyUseCase
import com.da.domain.useCases.SyncScreenUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { SaveScreenKeyUseCase(get()) }
    factory { GetScreenKeyUseCase(get()) }
    factory { SyncScreenUseCase(get(), get()) }
    factory { CheckPendingDownloadUseCase(get()) }
    factory { CleanTempFilesUseCase(get()) }
    factory { GetPlaylistForReplayUseCase(get(), get(), get()) }

    single { ScreenDiffCalculator() }
}