package com.da.data.downloader

import android.util.Log
import com.da.data.local.db.dao.ScreenDao
import com.da.data.local.db.entity.DownloadStatusEntity
import com.da.data.local.db.mapper.DownloadEntityMapper

class DownloadWorkerObserver(
    private val screenDao: ScreenDao,
    private val downloadEntityMapper: DownloadEntityMapper,
    private val playlistDownloader: PlaylistDownloader

) {
    suspend fun start() {
        try {
            val downloads = screenDao.getDownloads()
            for (d in downloads) {
                val download = downloadEntityMapper.map(d)
                val res = playlistDownloader.download(download)
                res.onSuccess {
                    screenDao.updateStatusAndPath(d.creativeKey, DownloadStatusEntity.DOWNLOADED, it.path)
                }.onFailure {
                    screenDao.updateStatus(d.creativeKey, DownloadStatusEntity.ERROR)
                }


                Log.e("DownloadWorkerObserver", "${download.creativeKey} ${res.isSuccess}")
            }
        } catch (e: Exception) {
            Log.e("DownloadWorkerObserver", e.toString())
        }


    }
}