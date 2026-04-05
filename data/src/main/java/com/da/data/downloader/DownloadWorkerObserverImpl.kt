package com.da.data.downloader

import android.util.Log
import com.da.domain.download.DownloadWorkerObserver
import com.da.domain.model.DownloadStatus
import com.da.domain.repository.DownloadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext




class DownloadWorkerObserverImpl(
    private val downloadRepository: DownloadRepository,
    private val playlistDownloader: PlaylistDownloader

) : DownloadWorkerObserver {

    override suspend fun clearTempFiles() = withContext(Dispatchers.IO) {
        playlistDownloader.clearTempFiles()
    }

    /**
     * check is there pending download in entity
     * download proc have 3 retry
     */
    override suspend fun checkPendingDownloads(): Unit = withContext(Dispatchers.IO) {
        
        try {
            val downloads = downloadRepository.getPendingDownloads()
            for (download in downloads){
                val res = playlistDownloader.downloadWithRetry(download)
                res.onSuccess {
                    downloadRepository.updateDownload(download.creativeKey, DownloadStatus.DOWNLOADED, it.path)
                }.onFailure {
                    downloadRepository.updateDownload(download.creativeKey, DownloadStatus.ERROR, null)
                }
            }
        }catch (e: Exception){
            Log.e("DownloadWorkerObserver", e.toString())
        }
    }


}