package com.da.data.remote.network

import com.da.data.remote.network.dto.ScreenResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

interface PlaylistApi {
    @GET("screen/playlistItems/{screenKey}")
    suspend fun getScreen(
        @Path("screenKey") screenKey: String
    ): Response<ScreenResponse>

    @GET("creative/get/{creativeKey}")
    @Streaming
    suspend fun downloadFile(@Path("creativeKey") creativeKey: String): Response<ResponseBody>
}