package com.da.domain.repository

import com.da.domain.model.Screen
import com.da.domain.model.ScreenResult

interface PlaylistRepository {

    suspend fun getRemoteScreen(screenKey: String): ScreenResult
    suspend fun getLocalScreen(screenKey: String): ScreenResult
    suspend fun fetchScreen(screen: Screen): ScreenResult
}