package com.da.data.remote.network

import com.da.data.remote.network.dto.ScreenResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PlaylistApi {
    @GET("screen/playlistItems/{screenKey}")
    suspend fun getScreen(
        @Path("screenKey") screenKey: String
    ): ScreenResponse
}