package com.da.data.remote.network.dto

import com.da.domain.model.Screen
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScreenResponse(
    val screenKey: String?,
    val playlists: List<PlaylistsResponse?>,
    val modified: Long?
)