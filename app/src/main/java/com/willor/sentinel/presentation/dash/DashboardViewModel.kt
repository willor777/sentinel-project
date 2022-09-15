package com.willor.sentinel.presentation.dash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willor.ktstockdata.marketdata.dataobjects.MajorFuturesData
import com.willor.ktstockdata.watchlistsdata.WatchlistOptions
import com.willor.ktstockdata.watchlistsdata.dataobjects.Watchlist
import com.willor.lib_data.data.local.preferences.AppPreferences
import com.willor.lib_data.data.local.preferences.DatastorePrefsManager
import com.willor.lib_data.domain.abstraction.IRepo
import com.willor.lib_data.domain.abstraction.Resource
import com.willor.lib_data.utils.handleErrorsToLog
import com.willor.sentinel.utils.periodicCoroutineRepeatOnFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject



@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repo: IRepo,
    private val prefsManager: DatastorePrefsManager
): ViewModel(){

    private val locTAG = DashboardViewModel::class.java.name

    private var _appPreferences = MutableStateFlow<AppPreferences?>(null)
    val appPreferences get() = _appPreferences.asStateFlow()

    private var _curSentinelWatchlist = MutableStateFlow<List<String>?>(null)
    val curSentinelWatchlist get() = _curSentinelWatchlist.asStateFlow()

    private var _futuresDataFlow = MutableStateFlow<MajorFuturesData?>(null)
    val futuresDataFlow get() = _futuresDataFlow.asStateFlow()
    private var futuresUpdater: Job     // Can be used to cancel the Futures Updater

    private var defaultWatchlistName = WatchlistOptions.MOST_ACTIVE
    private var _watchlistDataFlow = MutableStateFlow<Watchlist?>(null)
    val watchlistDataFlow get() = _watchlistDataFlow.asStateFlow()
    private var watchlistUpdater: Job

    init{
        futuresUpdater = periodicCoroutineRepeatOnFailure(
            delayTime = 60000,
            scope = viewModelScope,
            dispatcher = Dispatchers.IO
        ){
            updateFutures()
        }

        watchlistUpdater = periodicCoroutineRepeatOnFailure(
            delayTime = 60000,
            scope = viewModelScope,
            dispatcher = Dispatchers.IO
        ){
            loadYfWatchlist(defaultWatchlistName)
        }


        viewModelScope.launch(Dispatchers.IO){
            getAppPreferences()
        }

        loadSentinelWatchlist()
    }

    /**
     * Called to update the _futuresDataFlow.
     */
    private suspend fun updateFutures(): Boolean{

        // Flag showing whether or not task was a success
        var collected = false

        repo.getFuturesData().handleErrorsToLog().collect{
            when(it){
                is Resource.Loading -> {
                    Log.d("INFO", "Received Loading for getFuturesData()")
                }

                is Resource.Success -> {
                    _futuresDataFlow.value = it.data!!
                    collected = true
                    Log.d("INFO", "Collected futures data")
                }

                is Resource.Error -> {
                    Log.d("INFO", "Received Error for getFuturesData()")
                }
            }
        }
        return collected
    }


    private suspend fun getAppPreferences(){
        _appPreferences.value = prefsManager.getAppPreferences().first()
    }


    private fun saveAppPreferences(){
        viewModelScope.launch(Dispatchers.IO){
            prefsManager.saveToDatastore(appPreferences.value!!)
        }
    }


    /**
     * Used to load either "Most Active" "Big Gainers" "Big Losers"
     */
    private suspend fun loadYfWatchlist(wlName: WatchlistOptions? = null): Boolean{

        // Verify watchlist name
        val targetWatchlist = if (wlName == null){
            WatchlistOptions.MOST_ACTIVE
        } else{
            wlName
        }

        // Success flag
        var collected = false

        repo.getWatchlist(targetWatchlist).collect{
            when(it){
                is Resource.Loading -> {
                    Log.d("INFO", "Received Loading for getWatchlist()")
                }

                is Resource.Success -> {
                    _watchlistDataFlow.value = it.data
                    collected = true        // Set flag to true
                    Log.d("INFO", "Collected watchlist data")
                }

                is Resource.Error -> {
                    Log.d("INFO", "Received Error for getWatchlist()")
                }
            }
        }

        // Signal success / failure
        return collected
    }


    /**
     * Used to change what watchlist is displayed. Cancels the previous updater and creates a new
     * one.
     */
    fun changeYfWatchlist(wl: WatchlistOptions){
        viewModelScope.launch(Dispatchers.IO){

            // Cancel previous updater
            watchlistUpdater.cancelAndJoin()

            // Create new updater
            watchlistUpdater = periodicCoroutineRepeatOnFailure(
                delayTime = 60000,
                scope = viewModelScope,
                dispatcher = Dispatchers.IO
            ){
                loadYfWatchlist(wl)
            }
        }
    }


    private fun loadSentinelWatchlist(){
        viewModelScope.launch(Dispatchers.IO){
            val curSettings = prefsManager.getSentinelSettings().first()
            _curSentinelWatchlist.value = curSettings.currentWatchlist
        }
    }


    fun addTickerToSentinelWatchlist(ticker: String, failCallBack: ()->Unit){

        viewModelScope.launch(Dispatchers.IO) {
            val curSettings = prefsManager.getSentinelSettings().first()

            // Add ticker if it's not already on list
            if (!curSettings.currentWatchlist.contains(ticker)){
                curSettings.currentWatchlist.add(ticker)

                prefsManager.updateSentinelSettings(curSettings)

                _curSentinelWatchlist.value = curSettings.currentWatchlist
            }

            // Show toast if ticker is already on watchlist
            else{
                viewModelScope.launch(Dispatchers.Main){
                    failCallBack()
                }
            }
        }

    }


    fun removeTickerFromSentinelWatchlist(ticker: String){

        viewModelScope.launch(Dispatchers.IO) {
            val curSettings = prefsManager.getSentinelSettings().first()
            if (curSettings.currentWatchlist.contains(ticker)){
                curSettings.currentWatchlist.remove(ticker)
                prefsManager.updateSentinelSettings(curSettings)
                _curSentinelWatchlist.value = curSettings.currentWatchlist
            }
        }
    }


    /**
     * Cancels the updaters and end them
     */
    override fun onCleared() {
        Log.d("INFO", "$locTAG.onCleared() Called. Shutting of updaters")
        MainScope().launch(Dispatchers.IO){
            if (futuresUpdater.isActive){
                futuresUpdater.cancelAndJoin()
            }
            if (watchlistUpdater.isActive){
                watchlistUpdater.cancelAndJoin()
            }
        }
        super.onCleared()
    }

}

















