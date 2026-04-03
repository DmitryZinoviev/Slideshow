package com.da.data.network

import com.da.data.network.dto.ScreenResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PlaylistApi {
    @GET("screen/playlistItems/{screenKey}")
    suspend fun getScreen(
        @Path("screenKey") screenKey: String
    ): ScreenResponse
}