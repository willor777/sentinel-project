package com.willor.sentinel.presentation.sentinel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willor.lib_data.data.local.local_preferences.DatastorePrefsManager
import com.willor.lib_data.data.local.local_preferences.SentinelSettings
import com.willor.lib_data.domain.abstraction.IRepo
import com.willor.lib_data.domain.models.TriggerEntity
import com.willor.lib_data.utils.printToDEBUGTEMP
import com.willor.sentinel.utils.services.SentinelScannerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class SentinelViewModel @Inject constructor(
    private val repo: IRepo,
    private val prefsManager: DatastorePrefsManager,
): ViewModel() {

    private val locTAG = "SentinelViewModel"

    private var _sentinelSettings = MutableStateFlow<SentinelSettings?>(null)
    val sentinelSettings get() = _sentinelSettings.asStateFlow()

    private var sentinelScannerIsBound = false
    private var _sentinelScannerService = MutableStateFlow<SentinelScannerService?>(null)
    val sentinelScannerService get() = _sentinelScannerService.asStateFlow()

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
                printToDEBUGTEMP("$locTAG.loadSentinelSettings(): Collected sentinel settings")
            }
//            _sentinelSettings.value = prefsManager.getSentinelSettings().first()
        }
    }


    private fun saveSentinelSettings(sentinelSettings: SentinelSettings){
        viewModelScope.launch(Dispatchers.IO){
            prefsManager.updateSentinelSettings(sentinelSettings)
            printToDEBUGTEMP("$locTAG.saveSentinelSettings(): Saved sentinel settings")
        }
    }


    fun getWatchlistFromSentinelSettings(): List<String>?{
        return _sentinelSettings.value?.currentWatchlist
    }


    fun startSentinelScanner(context: Context){

        val serviceStart = {tickers: List<String>? ->

            val conn = object: ServiceConnection{
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    val binder = service as SentinelScannerService.SentinelScannerBinder
                    _sentinelScannerService.value = binder.getService()
                    sentinelScannerIsBound = true
                    Log.d("INFO", "$locTAG.startSentinelScanner() Called. Scanner has been bound")
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    sentinelScannerIsBound = false
                    _sentinelScannerService.value = null
                }
            }

            Intent(context, SentinelScannerService::class.java).also{intent ->
                context.startService(intent)
                context.bindService(intent, conn, Context.BIND_AUTO_CREATE)
            }
        }

        if (!sentinelScannerIsBound){

            viewModelScope.launch(Dispatchers.Unconfined){
                // Loop until watchlist can be retrieved
                while (_sentinelSettings.value == null){
                    delay(1000)
                }

                // Start service providing it with initial list of tickers
                serviceStart(getWatchlistFromSentinelSettings())
            }
        }
    }


}