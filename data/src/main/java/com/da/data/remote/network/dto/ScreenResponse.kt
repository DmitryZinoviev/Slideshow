package com.da.data.remote.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScreenResponse(
    val screenKey: String?,
    val company: String?,
    val breakpointInterval: Int?,
    val playlists: List<PlaylistsResponse?>,
    val modified: Long?
)