package com.da.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.da.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private val Context.userDataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepositoryImpl(
    private val context: Context
) : UserPreferencesRepository {

    override suspend fun getScreenKey(): String = withContext(Dispatchers.IO) {
        return@withContext context.userDataStore.data
            .map { it[SCREEN_KEY] ?: DEFAULT_SCREEN_KEY }
            .first()
    }

    override suspend fun saveScreenKey(key: String): Unit = withContext(Dispatchers.IO) {
        context.userDataStore.edit { it[SCREEN_KEY] = key }
    }

    override suspend fun getLastPlaylistKey(): String? = withContext(Dispatchers.IO) {
        return@withContext context.userDataStore.data
            .map { it[LAST_PLAYLIST_SCREEN_KEY] }
            .firstOrNull()
    }

    override suspend fun saveLastPlaylistKey(key: String) : Unit = withContext(Dispatchers.IO) {
        context.userDataStore.edit { it[LAST_PLAYLIST_SCREEN_KEY] = key }
    }

    suspend fun clear() = withContext(Dispatchers.IO) {
        context.userDataStore.edit { it.clear() }
    }

    companion object{
        val SCREEN_KEY = stringPreferencesKey("screen_key")
        val LAST_PLAYLIST_SCREEN_KEY = stringPreferencesKey("last_playlist_screen_key")
        const val DEFAULT_SCREEN_KEY = "7d47b6d7-8294-4b33-8887-066961d79993"
    }
}
