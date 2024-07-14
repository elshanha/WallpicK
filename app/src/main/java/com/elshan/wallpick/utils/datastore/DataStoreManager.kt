package com.elshan.wallpick.utils.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

object DataStoreManager {

    private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    private val IS_GRID_LAYOUT = booleanPreferencesKey("is_grid_layout")
    private val DOWNLOADED_WALLPAPERS = stringSetPreferencesKey("downloaded_wallpapers")
    private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")

    suspend fun setOnboardingCompleted(context: Context, completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }

    fun isOnboardingCompleted(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[ONBOARDING_COMPLETED] ?: false // Default to false if not set
            }
    }

    suspend fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }

    fun isNotificationsEnabled(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[NOTIFICATIONS_ENABLED] ?: true // Default to true if not set
            }
    }

    suspend fun setGridLayout(context: Context, isGrid: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_GRID_LAYOUT] = isGrid
        }
    }

    fun isGridLayout(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[IS_GRID_LAYOUT] ?: false
            }
    }

    suspend fun setWallpaperDownloaded(context: Context, wallpaperId: String, downloaded: Boolean) {
        context.dataStore.edit { preferences ->
            val currentDownloads = preferences[DOWNLOADED_WALLPAPERS] ?: emptySet()
            preferences[DOWNLOADED_WALLPAPERS] = if (downloaded) {
                currentDownloads + wallpaperId
            } else {
                currentDownloads - wallpaperId
            }
        }
    }

    fun isWallpaperDownloaded(context: Context, wallpaperId: String): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                val currentDownloads = preferences[DOWNLOADED_WALLPAPERS] ?: emptySet()
                currentDownloads.contains(wallpaperId)
            }
    }
}
