package com.da.data.downloader

import com.da.data.local.storage.FileStorage
import com.da.data.remote.network.PlaylistApi
import com.da.domain.model.Download
import java.io.File

class PlaylistDownloader(
    private val playlistApi: PlaylistApi,
    private val fileStorage: FileStorage

) {
    suspend fun download(download: Download): Result<File> {
        val key = download.creativeKey

        return try {
            if (fileStorage.exists(key)) {
                return Result.success(fileStorage.getFile(key))
            }

            fileStorage.ensureDir()

            val response = playlistApi.downloadFile(key)

            if (!response.isSuccessful) {
                return Result.failure(
                    IllegalStateException("Download failed: ${response.code()}")
                )
            }

            val body = response.body()
                ?: return Result.failure(IllegalStateException("Empty body"))

            val tmpFile = fileStorage.getTempFile(key)
            val finalFile = fileStorage.getFile(key)

            try {
                body.byteStream().use { input ->
                    tmpFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            } catch (e: Exception) {
                tmpFile.delete()
                return Result.failure(e)
            }


            if (!tmpFile.renameTo(finalFile)) {
                tmpFile.delete()
                return Result.failure(IllegalStateException("Rename failed"))
            }

            Result.success(finalFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun downloadWithRetry(download: Download): Result<File> {
        repeat(3) { attempt ->
            val result = download(download)
            if (result.isSuccess) return result
        }
        return Result.failure(Exception("Failed after retries"))
    }

    suspend fun clearTempFiles(){
        fileStorage.clearTempFiles()
    }
}
