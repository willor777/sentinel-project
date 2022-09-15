package com.willor.sentinel.presentation.sentinel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willor.lib_data.data.local.preferences.DatastorePrefsManager
import com.willor.lib_data.data.local.preferences.SentinelSettings
import com.willor.lib_data.domain.abstraction.IRepo
import com.willor.lib_data.domain.models.TriggerEntity
import com.willor.sentinel.utils.services.SentinelScannerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SentinelViewModel @Inject constructor(
    private val repo: IRepo,
    private val prefsManager: DatastorePrefsManager,
): ViewModel() {

    private val locTAG = "SentinelViewModel"

    private var _sentinelSettings = MutableStateFlow<SentinelSettings?>(null)
    val sentinelSettings get() = _sentinelSettings.asStateFlow()

    private var _recentTriggersStateFlow = MutableStateFlow<List<TriggerEntity>>(listOf())
    val recentTriggersStateFlow get() = _recentTriggersStateFlow.asStateFlow()


    init{
        loadSentinelSettings()
        loadRecentTriggers()
    }


    private fun loadRecentTriggers(){
        viewModelScope.launch(Dispatchers.IO){
            repo.getAllTriggersWithinTimeRange(null, null).collect{
                _recentTriggersStateFlow.value = it ?: listOf()
                Log.d("INFO", "$locTAG.loadRecentTriggers() collected db triggers." +
                        " Size of Triggers: ${it?.size ?: 0}")
            }
        }
    }


    private fun loadSentinelSettings(){
        viewModelScope.launch(Dispatchers.IO){

            prefsManager.getSentinelSettings().collectLatest {
                _sentinelSettings.value = it
            }
//            _sentinelSettings.value = prefsManager.getSentinelSettings().first()
        }
    }


    private fun saveSentinelSettings(sentinelSettings: SentinelSettings){
        viewModelScope.launch(Dispatchers.IO){
            prefsManager.updateSentinelSettings(sentinelSettings)
        }
    }


    fun getWatchlistFromSentinelSettings(): List<String>?{
        return _sentinelSettings.value?.currentWatchlist
    }


    fun startSentinelScanner(context: Context){
        viewModelScope.launch(Dispatchers.Unconfined){
            // Loop until watchlist can be retrieved
            while (_sentinelSettings.value == null){
                delay(1000)
            }

            // Start service
            Intent(context, SentinelScannerService::class.java).also{intent ->
                context.startService(intent)
            }
        }
    }
}