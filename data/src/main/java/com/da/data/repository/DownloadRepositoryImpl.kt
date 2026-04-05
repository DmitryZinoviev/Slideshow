package com.da.data.repository

import android.util.Log
import com.da.data.local.db.dao.DownloadDao
import com.da.data.local.db.mapper.DownloadEntityMapper
import com.da.data.local.db.mapper.toEntity
import com.da.domain.model.Download
import com.da.domain.model.DownloadStatus
import com.da.domain.model.ScreenResult
import com.da.domain.repository.DownloadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.Path


class DownloadRepositoryImpl(
    private val downloadDao: DownloadDao,
    private val downloadEntityMapper: DownloadEntityMapper
) : DownloadRepository {

    override suspend fun getPendingDownloads(): List<Download> = withContext(Dispatchers.IO){
        val result = mutableListOf<Download>()
        try {
            val pendingDownloads = downloadDao.getPendingDownloads()
            for (d in pendingDownloads){
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
        downloadDao.updateStatusAndPath(key, status.toEntity(), path)
        Log.d("updateDownload","Update $key, $status, ${path.orEmpty()}")
    }
}