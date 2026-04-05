package com.da.domain.repository

interface UserPreferencesRepository {
    suspend fun getScreenKey(): String
    suspend fun saveScreenKey(key: String)
    suspend fun getLastPlaylistKey(): String?
    suspend fun saveLastPlaylistKey(key: String)
}