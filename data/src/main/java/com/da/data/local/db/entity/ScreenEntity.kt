package com.da.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "screens")
data class ScreenEntity(
    @PrimaryKey
    val screenKey: String,
    val modified: Long?,
    val isDownloaded: Boolean

)