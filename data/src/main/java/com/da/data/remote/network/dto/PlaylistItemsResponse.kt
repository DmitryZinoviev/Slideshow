package com.da.data.remote.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaylistItemsResponse(
    val duration: Int?,
    val dataSize: Int?,
    val modified: Long?,
    val creativeLabel: String?,
    val playlistKey: String?,
    val creativeKey: String?,
    val orderKey: Int?,
)