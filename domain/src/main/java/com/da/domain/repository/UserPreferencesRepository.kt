package com.da.domain.repository

interface UserPreferencesRepository {
    suspend fun getScreenKey(): String
    suspend fun saveScreenKey(key: String)
}