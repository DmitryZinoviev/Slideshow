package com.da.data.local.storage

import android.content.Context
import java.io.File

class FileStorage(private val context: Context)  {

    private val mediaDir: File
        get() = File(context.filesDir, "media")

    fun ensureDir() {
        //TODO check if it is created
        if (!mediaDir.exists()) {
            mediaDir.mkdirs()
        }
    }

    fun getFile(key: String): File {
        ensureDir()
        return File(mediaDir, key)
    }

    fun getTempFile(key: String): File {
        ensureDir()
        return File(mediaDir, "$key.tmp")
    }

    fun exists(key: String): Boolean {
        return getFile(key).exists()
    }

    fun delete(key: String) {
        getFile(key).delete()
        getTempFile(key).delete()
    }

    fun clearTempFiles() {
        ensureDir()
        mediaDir.listFiles()?.forEach { file ->
            if (file.isFile && file.name.endsWith(".tmp")) {
                file.delete()
            }
        }
    }
}