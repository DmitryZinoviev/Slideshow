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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
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


    /**
     * download 3 items simultaneously
     * @return Result<Pair<Int,Int>> first success count, amount of downloads
     */
    override suspend fun downloadScreen(screenKey: String): Result<Pair<Int,Int>> =
        withContext(Dispatchers.IO) {
            coroutineScope {
                val screen = screenDao.getScreenWithPlaylists(screenKey)
                    ?: return@coroutineScope Result.failure(Exception("cant load data"))

                val playlistItems = screen.playlists.flatMap { it.items }

                val downloads = playlistItems
                    .filter { it.creativeKey != null }
                    .distinctBy { it.creativeKey } // бонус — прибираємо дублікати
                    .map {
                        DownloadEntity(
                            creativeKey = it.creativeKey!!,
                            localPath = null,
                            status = DownloadStatusEntity.NOT_DOWNLOADED
                        )
                    }

                downloadDao.insertDownloads(downloads)

                val downloadEntities = downloadDao.getDownloads(
                    downloads.map { it.creativeKey }.distinct(),
                    DownloadStatus.NOT_DOWNLOADED)

                val semaphore = Semaphore(3)

                val jobs = downloadEntities.map { entity ->
                    async {
                        semaphore.withPermit {
                            val res = download(entity)

                                res.onSuccess { file ->
                                    downloadDao.updateStatusAndPath(
                                        entity.creativeKey,
                                        status = DownloadStatusEntity.DOWNLOADED,
                                        path = file.path
                                    )
                                }
                                .getOrElse {
                                    downloadDao.updateStatusAndPath(
                                        entity.creativeKey,
                                        status = DownloadStatusEntity.ERROR,
                                        path = null
                                    )
                                }
                            return@async if (res.isSuccess) 1 else 0
                        }
                    }
                }

                val successCount = jobs.awaitAll().sum()

                updateScreenAfterDownload()

                Result.success(Pair(successCount, downloadEntities.count()))
            }
        }

    /**
     * will update all screens where playlist downloaded
     */
    private suspend fun updateScreenAfterDownload(){
        screenDao.updateScreensIfAllPlaylistsDownloaded()
    }

    private suspend fun download(downloadEntity: DownloadEntity): Result<File> {
        val download = downloadEntityMapper.map(downloadEntity)
        return downloadWorker.download(download)
    }
}