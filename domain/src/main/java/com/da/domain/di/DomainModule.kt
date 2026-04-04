package com.da.domain.di

import com.da.domain.useCases.GetScreenKeyUseCase
import com.da.domain.useCases.SaveScreenKeyUseCase
import com.da.domain.useCases.SyncScreenUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { SaveScreenKeyUseCase(get()) }
    factory { GetScreenKeyUseCase(get()) }
    factory { SyncScreenUseCase(get()) }
}