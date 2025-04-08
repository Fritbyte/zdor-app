package com.zdor.application.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

private val Context.dataStore by preferencesDataStore("auth_prefs")

class TokenManager(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val TOKEN_EXPIRY_KEY = stringPreferencesKey("token_expiry")
        private const val DEFAULT_TOKEN_LIFETIME = 3600000L
    }

    suspend fun saveToken(
        token: String,
        expiryTime: Long = System.currentTimeMillis() + DEFAULT_TOKEN_LIFETIME
    ) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[TOKEN_KEY] = token
                preferences[TOKEN_EXPIRY_KEY] = expiryTime.toString()
            }
        }
    }

    suspend fun getToken(): String? = withContext(Dispatchers.IO) {
        val preferences = context.dataStore.data.first()

        val token = preferences[TOKEN_KEY]
        val expiryTime = preferences[TOKEN_EXPIRY_KEY]?.toLongOrNull()

        if (token != null && expiryTime != null && expiryTime > System.currentTimeMillis()) {
            token
        } else {
            clearToken()
            null
        }
    }

    suspend fun hasValidToken(): Boolean = getToken() != null

    suspend fun clearToken() {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences.remove(TOKEN_KEY)
                preferences.remove(TOKEN_EXPIRY_KEY)
            }
        }
    }

    suspend fun refreshTokenExpiry() = withContext(Dispatchers.IO) {
        val token = getToken()
        if (token != null) {
            saveToken(token)
        }
    }
}
