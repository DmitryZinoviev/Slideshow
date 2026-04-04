package com.da.data.repository

import com.da.data.local.db.dao.ScreenDao
import com.da.data.local.db.entity.toEntities
import com.da.data.local.db.mapper.ScreenEntityMapper
import com.da.data.local.db.mapper.ScreenWithPlaylistsMapper
import com.da.data.remote.network.PlaylistDataSource
import com.da.data.remote.network.mapper.ScreenMapper
import com.da.domain.core.ApiResult
import com.da.domain.model.FetchResult
import com.da.domain.model.Screen
import com.da.domain.model.ScreenResult
import com.da.domain.repository.PlaylistRepository

class PlaylistRepositoryImpl(
    private val screenDao: ScreenDao,
    private val playlistDataSource: PlaylistDataSource,
    private val screenMapper: ScreenMapper,
    private val screenWithPlaylistsMapper: ScreenWithPlaylistsMapper
): PlaylistRepository {
    override suspend fun getRemoteScreen(screenKey: String): ScreenResult {
        try {
            val result = playlistDataSource.getPlaylist(screenKey)
            if(result is ApiResult.Success){
                val screen = screenMapper.map(result.data)
                return ScreenResult.Found(screen)
            }else{
                return ScreenResult.Fail(Exception("fail"))
            }
        }catch (e: Exception){
            return ScreenResult.Fail(e)
        }

    }

    override suspend fun getLocalScreen(screenKey: String): ScreenResult {
        try {
            val result = screenDao.getScreenWithPlaylists(screenKey)
            result?.let {
                val screen = screenWithPlaylistsMapper.map(it)
                return ScreenResult.Found(screen)
            }
            return ScreenResult.Fail(Exception("Not found"))

        } catch (e: Exception) {
            return ScreenResult.Fail(e)
        }
    }
//    override suspend fun syncScreen(screenKey: String): ApiResult<Screen> {
//        try {
//            val result = playlistDataSource.getPlaylist(screenKey)
//            if(result is ApiResult.Success){
//
//                val (screen, playlist, items) = result.data.toEntities()
//                screenDao.insertFullScreen(screen, playlist, items)
//            }
//        }catch (e: Exception){
//            return ApiResult.UnknownError(e)
//        }
//
//        return ApiResult.Success(Screen())
//
//    }
//
//
//    suspend fun fetchIfNeeded(screenKey: String): FetchResult{
//        try {
//            val result = playlistDataSource.getPlaylist(screenKey)
//            if(result is ApiResult.Success){
//                val screen = screenDao.getScreenByKey(screenKey)
//                if(screen?.modified == result.data.modified){
//                    return FetchResult.Skip
//                }
//
//                //val (, playlist, items) = result.data.toEntities()
//                screenDao.insertFullScreen(screen, playlist, items)
//                return FetchResult.Fetch
//            }
//
//        }catch (e: Exception){
//            return FetchResult.Error
//        }
//    }
//
//    suspend fun isFetchNeeded(screenDao: ScreenDao): FetchResult{
//        try {
//            val result = playlistDataSource.getPlaylist(screenKey)
//            if(result is ApiResult.Success){
//                val screen = screenDao.getScreenByKey(screenKey)
//                if(screen?.modified == result.data.modified){
//                    return FetchResult.Skip
//                }
//
//                //val (, playlist, items) = result.data.toEntities()
//                screenDao.insertFullScreen(screen, playlist, items)
//                return FetchResult.Fetch
//            }
//
//        }catch (e: Exception){
//            return FetchResult.Error
//        }
//    }




}