package com.da.data.repository

import android.util.Log
import com.da.data.local.db.dao.DownloadDao
import com.da.data.local.db.dao.ScreenDao
import com.da.data.local.db.entity.DownloadEntity
import com.da.data.local.db.entity.DownloadStatusEntity
import com.da.data.local.db.mapper.DownloadEntityMapper
import com.da.data.local.db.mapper.toEntity
import com.da.domain.download.DownloadWorker
import com.da.domain.model.Download
import com.da.domain.model.DownloadStatus
import com.da.domain.repository.DownloadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


class DownloadRepositoryImpl(
    private val downloadDao: DownloadDao,
    private val downloadEntityMapper: DownloadEntityMapper,
    private val screenDao: ScreenDao,
    private val downloadWorker: DownloadWorker
) : DownloadRepository {

    override suspend fun getPendingDownloads(): List<Download> = withContext(Dispatchers.IO) {
        val result = mutableListOf<Download>()
        try {
            val pendingDownloads = downloadDao.getPendingDownloads()
            for (d in pendingDownloads) {
                val download = downloadEntityMapper.map(d)
                result.add(download)
            }

        } catch (e: Exception) {
            Log.e("DownloadRepositoryImpl", e.toString())
        }
        result
    }

    override suspend fun updateDownload(
        key: String,
        status: DownloadStatus,
        path: String?
    ): Unit = withContext(Dispatchers.IO) {
        try {
            downloadDao.updateStatusAndPath(key, status.toEntity(), path)
            Log.d("updateDownload", "Update $key, $status, ${path.orEmpty()}")
        } catch (e: Exception) {
            Log.e("updateDownload", "Fail $key, $status, ${path.orEmpty()} ${e.toString()}")
        }
    }

    override suspend fun getDownloads(keys: List<String>): List<Download> =
        withContext(Dispatchers.IO) {
            try {
                val downloadEntities = downloadDao.getDownloads(keys, DownloadStatus.DOWNLOADED)
                return@withContext downloadEntities.map(downloadEntityMapper::map)
            } catch (e: Exception) {
                Log.e("getDownloads", "Fail $keys; ${e.toString()}")
            }
            emptyList()
        }

    override suspend fun downloadScreen(screenKey: String): Result<Int> {
        val screen = screenDao.getScreenWithPlaylists(screenKey) ?: return Result.failure(
            Exception(
                "cant load data"
            )
        )
//        val duplicates = items
//            .filter { it.creativeKey != null }
//            .groupBy { it.creativeKey }
//            .filter { it.value.size > 1 }
//            .flatMap { it.value }
        val playlistItems = screen.playlists.flatMap { it.items }.map { it }
        val downloads = playlistItems
            .filter { it.creativeKey != null }
            .map {
                DownloadEntity(
                    creativeKey = it.creativeKey!!,
                    localPath = null,
                    status = DownloadStatusEntity.NOT_DOWNLOADED
                )
            }
        val downloadEntities = downloadDao.insertDownloads(downloads)
        var successCount = 0
        for(d in downloadEntities){
            download(d).onSuccess {
                successCount++
                downloadDao.updateStatusAndPath(
                    d.creativeKey,
                    status = DownloadStatusEntity.DOWNLOADED,
                    path = it.path
                )
            }

        }
        return Result.success(successCount)
    }

    private suspend fun download(downloadEntity: DownloadEntity): Result<File> {
        val download = downloadEntityMapper.map(downloadEntity)
        return downloadWorker.download(download)
    }
}