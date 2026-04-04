package com.da.data.di

import com.da.data.remote.network.MoshiFactory
import com.da.data.remote.network.OkHttpFactory
import com.da.data.remote.network.PlaylistApi
import com.da.data.remote.network.RetrofitFactory
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {

    single { OkHttpFactory.create() }

    single { MoshiFactory.create() }

    single {
        RetrofitFactory.create(
            okHttpClient = get(),
            moshi = get()
        )
    }

    single<PlaylistApi> {
        get<Retrofit>().create(PlaylistApi::class.java)
    }
}