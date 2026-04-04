package com.da.data.remote.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaylistsResponse(
    val channelTime: Int?,
    val playlistItems: List<PlaylistItemsResponse?>,
    val playlistKey: String?
)