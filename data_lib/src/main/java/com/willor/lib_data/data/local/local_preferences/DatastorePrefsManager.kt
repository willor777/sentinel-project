package com.willor.lib_data.data.local.local_preferences

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

const val USER_PREFERENCES_NAME = "userprefs"

private val Context.datastore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class DatastorePrefsManager(private val context: Context) {

    suspend fun saveToDatastore(appPreferences: AppPreferences){
        context.datastore.edit {
            it[SENTINEL_SETTINGS_JSON] =
                appPreferences.sentinelSettingsJson ?: SentinelSettings().toJson()
        }
    }

    fun getAppPreferences(): Flow<AppPreferences> {
        return context.datastore.data.map {
            AppPreferences(
                sentinelSettingsJson = it[SENTINEL_SETTINGS_JSON]
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

    suspend fun updateSentinelLastScanTime(scanTime: Long){
        // Load cur settings
        val appPrefs = getAppPreferences().first()
        val sentSettings = SentinelSettings.fromJson(appPrefs.sentinelSettingsJson!!)
        sentSettings.lastScan = scanTime
        appPrefs.sentinelSettingsJson = sentSettings.toJson()
        saveToDatastore(appPrefs)
    }


    companion object PreferenceKeys{
        val SENTINEL_SETTINGS_JSON = stringPreferencesKey("SENTINEL_SETTINGS_JSON")
    }
}