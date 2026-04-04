package com.da.data.local.storage

import android.content.Context
import java.io.File

interface FileStorage {
    fun getFile(key: String): File
    fun getTempFile(key: String): File
    fun exists(key: String): Boolean
    fun delete(key: String)
    fun ensureDir()
}

class FileStorageImpl(private val context: Context) : FileStorage {

    private val mediaDir: File
        get() = File(context.filesDir, "media")

    override fun ensureDir() {
        //TODO check if it is created
        if (!mediaDir.exists()) {
            mediaDir.mkdirs()
        }
    }

    override fun getFile(key: String): File {
        ensureDir()
        return File(mediaDir, key)
    }

    override fun getTempFile(key: String): File {
        ensureDir()
        return File(mediaDir, "$key.tmp")
    }

    override fun exists(key: String): Boolean {
        return getFile(key).exists()
    }

    override fun delete(key: String) {
        getFile(key).delete()
        getTempFile(key).delete()
    }
}