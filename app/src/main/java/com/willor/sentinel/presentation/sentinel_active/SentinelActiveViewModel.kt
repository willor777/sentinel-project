package com.willor.sentinel.presentation.sentinel_active

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willor.lib_data.data.local.local_preferences.DatastorePrefsManager
import com.willor.lib_data.data.local.local_preferences.SentinelSettings
import com.willor.lib_data.domain.abstraction.IRepo
import com.willor.lib_data.utils.printToDEBUGTEMP
import com.willor.sentinel_bots.bots.DoubleStepBot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SentinelActiveViewModel @Inject constructor(
    private val repo: IRepo,
    private val prefsManager: DatastorePrefsManager,
    private val doubleStepBot: DoubleStepBot
): ViewModel() {

    private var _sentinelSettings = MutableStateFlow<SentinelSettings?>(null)
    val sentinelSettings get() = _sentinelSettings.asStateFlow()



    init{
        loadSentinelSettings()
    }


    private fun loadSentinelSettings(){
        viewModelScope.launch(Dispatchers.IO){
            _sentinelSettings.value = prefsManager.getSentinelSettings().first()
            printToDEBUGTEMP("SentinelActiveViewModel: Collected sentinel settings")
        }
    }


    private fun saveSentinelSettings(sentinelSettings: SentinelSettings){
        viewModelScope.launch(Dispatchers.IO){
            prefsManager.updateSentinelSettings(sentinelSettings)
            printToDEBUGTEMP("SentinelActiveViewModel: Saved sentinel settings")
        }
    }


    private fun botTest(){
        viewModelScope.launch(Dispatchers.IO){
            printToDEBUGTEMP("SentActiveViewModel: About to run bot")
            val results = doubleStepBot.runScan(_sentinelSettings.value!!.currentWatchlist)
            printToDEBUGTEMP("SentActiveViewModel: Bot Ran...Results: $results")
        }
    }

}