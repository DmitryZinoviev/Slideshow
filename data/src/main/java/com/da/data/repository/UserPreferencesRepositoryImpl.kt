package com.da.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.da.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.userDataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepositoryImpl(private val context: Context) : UserPreferencesRepository {

    override suspend fun getScreenKey(): String {
        return context.userDataStore.data
            .map { it[SCREEN_KEY] ?: DEFAULT_SCREEN_KEY }
            .first()
    }

    override suspend fun saveScreenKey(key: String) {
        context.userDataStore.edit { it[SCREEN_KEY] = key }
    }

    suspend fun clear() {
        context.userDataStore.edit { it.clear() }
    }

    companion object{
        val SCREEN_KEY = stringPreferencesKey("screen_key")
        const val DEFAULT_SCREEN_KEY = "7d47b6d7-8294-4b33-8887-066961d79993"
    }
}
