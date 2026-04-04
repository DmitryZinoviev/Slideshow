package com.da.data.remote.network

import com.da.data.remote.network.dto.ScreenResponse
import com.da.domain.core.ApiResult
import java.io.IOException

class PlaylistDataSource(
    private val playlistApi: PlaylistApi
) {
    suspend fun getPlaylist(screenKey: String): ApiResult<ScreenResponse> {
        try {
            val response = playlistApi.getScreen(screenKey)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return ApiResult.Success(body)
                } else {
                    return ApiResult.UnknownError(NullPointerException("Body is null"))
                }
            } else {
                return ApiResult.HttpError(
                    code = response.code(),
                    body = response.errorBody()?.string()
                )
            }
        } catch (e: IOException) {
            return ApiResult.NetworkError(e)
        } catch (e: Exception) {
            return ApiResult.UnknownError(e)
        }
    }
}