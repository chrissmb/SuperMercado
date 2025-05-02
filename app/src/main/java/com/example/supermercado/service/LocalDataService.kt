package com.example.supermercado.service

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataService {

    val Context.dataStore by preferencesDataStore(name = "settings")

    val TOKEN = stringPreferencesKey("token")
    val EXPIRES_IN = intPreferencesKey("expires_in")

    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    fun getToken(context: Context): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    suspend fun saveExpiresIn(context: Context, expiresIn: Int) {
        context.dataStore.edit { preferences ->
            preferences[EXPIRES_IN] = expiresIn
        }
    }

    fun getExpiresIn(context: Context): Flow<Int> {
        return context.dataStore.data.map { preferences ->
            preferences[EXPIRES_IN] ?: 0
        }
    }
}