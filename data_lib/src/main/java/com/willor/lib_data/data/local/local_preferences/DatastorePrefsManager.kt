package com.willor.lib_data.data.local.local_preferences

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.willor.lib_data.data.local.local_preferences.DatastorePrefsManager.PreferenceKeys.SENTINEL_LAST_SCAN_TIME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception

const val USER_PREFERENCES_NAME = "userprefs"

private val Context.datastore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class DatastorePrefsManager(private val context: Context) {

    suspend fun saveToDatastore(appPreferences: AppPreferences){
        context.datastore.edit {
            it[SENTINEL_LAST_SCAN_TIME] = appPreferences.sentinelLastScan ?: 0
        }
    }

    fun getFromDatastore(): Flow<AppPreferences> {
        return context.datastore.data.map {
            AppPreferences(
                sentinelLastScan = it[SENTINEL_LAST_SCAN_TIME] ?: 0
            )
        }
    }

    suspend fun updateSentinelSettings(sentinelSettings: SentinelSettings){
        context.datastore.edit {
            it[SENTINEL_SETTINGS_JSON] = sentinelSettings.toJson()
        }
    }

    fun getSentinelSettings(): Flow<SentinelSettings> {
        return context.datastore.data.map{
            val settingsJson = it[SENTINEL_SETTINGS_JSON] ?: ""
            if (settingsJson != ""){
                SentinelSettings.fromJson(settingsJson)
            }else{
                Log.d("INFO", "Warning! No SentinelSettings() Found... Returning" +
                        "Default SentinelSettings()")
                SentinelSettings()
            }
        }
    }


    companion object PreferenceKeys{
        val SENTINEL_LAST_SCAN_TIME = longPreferencesKey("LAST_SCAN_TIME")
        val SENTINEL_SETTINGS_JSON = stringPreferencesKey("SENTINEL_SETTINGS_JSON")
    }
}