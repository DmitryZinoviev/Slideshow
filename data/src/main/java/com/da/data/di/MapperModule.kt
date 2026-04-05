package com.da.data.di

import com.da.data.local.db.mapper.DownloadEntityMapper
import com.da.data.local.db.mapper.PlaylistEntityMapper
import com.da.data.local.db.mapper.PlaylistItemEntityMapper
import com.da.data.local.db.mapper.ScreenEntityMapper
import com.da.data.local.db.mapper.ScreenToEntityMapper
import com.da.data.local.db.mapper.ScreenWithPlaylistsMapper
import com.da.data.remote.network.MoshiFactory
import com.da.data.remote.network.OkHttpFactory
import com.da.data.remote.network.PlaylistApi
import com.da.data.remote.network.PlaylistDataSource
import com.da.data.remote.network.RetrofitFactory
import com.da.data.remote.network.mapper.PlaylistItemMapper
import com.da.data.remote.network.mapper.PlaylistMapper
import com.da.data.remote.network.mapper.ScreenMapper
import com.da.domain.model.PlaylistItem
import org.koin.dsl.module
import retrofit2.Retrofit

val mapperModule = module {

    single { ScreenMapper(get()) }
    single { PlaylistMapper(get()) }
    single { PlaylistItemMapper() }

    single { ScreenEntityMapper(get()) }
    single { PlaylistEntityMapper(get()) }
    single { PlaylistItemEntityMapper() }
    single { ScreenWithPlaylistsMapper() }
    single { ScreenToEntityMapper() }
    single { DownloadEntityMapper() }




}