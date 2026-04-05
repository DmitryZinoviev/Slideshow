package com.da.data.repository

import androidx.room.withTransaction
import com.da.data.local.db.AppDatabase
import com.da.data.local.db.dao.ScreenDao
import com.da.data.local.db.mapper.ScreenToEntityMapper
import com.da.data.local.db.mapper.ScreenWithPlaylistsMapper
import com.da.data.remote.network.PlaylistDataSource
import com.da.data.remote.network.mapper.ScreenMapper
import com.da.domain.core.ApiResult
import com.da.domain.diff.ScreenDiff
import com.da.domain.model.Screen
import com.da.domain.model.ScreenResult
import com.da.domain.repository.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val db: AppDatabase,
    private val screenDao: ScreenDao,
    private val playlistDataSource: PlaylistDataSource,
    private val screenMapper: ScreenMapper,
    private val screenWithPlaylistsMapper: ScreenWithPlaylistsMapper,
    private val screenToEntityMapper: ScreenToEntityMapper
): PlaylistRepository {
    override suspend fun getRemoteScreen(
        screenKey: String
    ): ScreenResult = withContext(Dispatchers.IO) {
        try {
            val result = playlistDataSource.getPlaylist(screenKey)
            if(result is ApiResult.Success){
                val screen = screenMapper.map(result.data)
                return@withContext ScreenResult.Success(screen)
            }else{
                return@withContext ScreenResult.Error(Exception("fail"))
            }
        }catch (e: Exception){
            return@withContext ScreenResult.Error(e)
        }

    }

    override suspend fun getLocalScreen(screenKey: String): ScreenResult = withContext(Dispatchers.IO) {
        try {
            val result = screenDao.getScreenWithPlaylists(screenKey)
            result?.let {
                val screen = screenWithPlaylistsMapper.map(it)
                return@withContext ScreenResult.Success(screen)
            }
            return@withContext ScreenResult.NotFound

        } catch (e: Exception) {
            return@withContext ScreenResult.Error(e)
        }
    }

    override suspend fun fetchScreen(screen: Screen): ScreenResult {
        try {
            val (screenEntity, playlistEntity, itemsEntity) = screenToEntityMapper.map(screen)
            screenDao.insertFullScreen(screenEntity, playlistEntity, itemsEntity)
            return ScreenResult.Success(screen)
        } catch (e: Exception) {
            return ScreenResult.Error(e)
        }
    }

}