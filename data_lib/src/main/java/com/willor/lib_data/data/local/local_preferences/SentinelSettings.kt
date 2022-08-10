package com.willor.lib_data.data.local.local_preferences

import com.google.gson.Gson
import java.lang.Exception

data class SentinelSettings(
    val scanInterval: Long = 60_000,
    val currentWatchlist: MutableList<String> = mutableListOf(),
    val currentScanAlgo: String = "BASIC",
){
    companion object{
        fun fromJson(json: String): SentinelSettings{
            return try{
                Gson().fromJson(json, SentinelSettings::class.java)
            }catch(e: Exception){
                SentinelSettings()
            }
        }
    }
}

fun SentinelSettings.toJson(): String{
    return Gson().toJson(this)
}

