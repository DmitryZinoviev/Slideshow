package com.da.domain.repository

import com.da.domain.model.Download
import com.da.domain.model.DownloadStatus
import java.nio.file.Path

interface DownloadRepository{
    suspend fun getPendingDownloads(): List<Download>
    suspend fun updateDownload(key: String, status: DownloadStatus, path: String?)
    suspend fun getDownloads(keys: List<String>): List<Download>
    suspend fun downloadScreen(screenKey: String): Result<Pair<Int,Int>>
}