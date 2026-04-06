package com.da.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.da.data.local.db.entity.DownloadEntity
import com.da.data.local.db.entity.DownloadStatusEntity
import com.da.domain.model.DownloadStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDownloads(items: List<DownloadEntity>): List<DownloadEntity>
    @Query("SELECT * FROM downloads")
    fun observeAll(): Flow<List<DownloadEntity>>

    @Transaction
    @Query("SELECT * FROM downloads")
    suspend fun getDownloads(): List<DownloadEntity>

    @Transaction
    @Query("SELECT * FROM downloads WHERE status=:status and creativeKey IN (:keys)")
    suspend fun getDownloads(keys: List<String>, status: DownloadStatus): List<DownloadEntity>

    @Query("UPDATE downloads SET status = :status WHERE creativeKey = :key")
    suspend fun updateStatus(
        key: String,
        status: DownloadStatusEntity
    )

    @Query("""
        UPDATE downloads 
        SET status = :status, localPath = :path 
        WHERE creativeKey = :key
    """)
    suspend fun updateStatusAndPath(
        key: String,
        status: DownloadStatusEntity,
        path: String?
    )

    @Query("SELECT * FROM downloads WHERE status = :status")
    fun observeByStatus(status: DownloadStatusEntity): Flow<List<DownloadEntity>>


    @Transaction
    @Query("SELECT * FROM downloads WHERE status != :status")
    suspend fun getPendingDownloads(status: DownloadStatusEntity = DownloadStatusEntity.NOT_DOWNLOADED): List<DownloadEntity>

}