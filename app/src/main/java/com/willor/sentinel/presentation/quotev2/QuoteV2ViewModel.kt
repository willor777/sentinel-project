package com.willor.sentinel.presentation.quotev2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willor.ktstockdata.marketdata.dataobjects.EtfQuote
import com.willor.ktstockdata.marketdata.dataobjects.OptionStats
import com.willor.ktstockdata.marketdata.dataobjects.StockQuote
import com.willor.lib_data.StockSymbolsLoader
import com.willor.lib_data.data.local.preferences.DatastorePrefsManager
import com.willor.lib_data.data.local.preferences.SentinelSettings
import com.willor.lib_data.domain.abstraction.IRepo
import com.willor.lib_data.domain.abstraction.Resource
import com.willor.sentinel.utils.periodicCoroutineRepeatOnFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuoteV2ViewModel @Inject constructor(
    private val repo: IRepo,
    private val prefs: DatastorePrefsManager
): ViewModel() {

    // Location tag is class name
    private val locTAG = this::class.java.name

    /**
     * State Flows
     */

    // StockQuote, EtfQuote, OptionStats
    private var _stockQuote = MutableStateFlow<StockQuote?>(null)
    val stockQuoteFlow get() = _stockQuote.asStateFlow()

    private var _etfQuote = MutableStateFlow<EtfQuote?>(null)
    val etfQuoteFlow get() = _etfQuote.asStateFlow()

    private var _optionStats = MutableStateFlow<OptionStats?>(null)
    val optionStatsFlow get() = _optionStats.asStateFlow()

    // Flags showing if retrieval attempts have stopped
    private var _quoteFailed = MutableStateFlow(false)
    val quoteFailed get() = _quoteFailed.asStateFlow()

    private var _optionStatsFailed = MutableStateFlow(false)
    val optionStatsFailed get() = _optionStatsFailed.asStateFlow()

    // Sentinel Settings (for watchlist)
    private var _sentinelSettings = MutableStateFlow<SentinelSettings?>(null)
    val sentinelSettings get() = _sentinelSettings.asStateFlow()

    // Search Results + List of symbols
    private var _searchMatchesV2 = MutableStateFlow(listOf<List<String>>())
    val searchResults get() = _searchMatchesV2.asStateFlow()

    private lateinit var stockSymbolsList: List<List<String>>


    /**
     * Updater Stuff
     */

    // Used by both jobs for updates
    private var curTicker = "NONE"
    private var quoteType = ""

    // Update Intervals
    private val quoteUpdateInterval: Long = 60_000        // TODO Replace with settings
    private val optionUpdateInterval: Long = 300_000      // TODO Ditto

    // Data Failed To Load Stuff

    private val maxQuoteFails = 3
    private val maxOptionFails = 3
    private var curQuoteFails = 0
    private var curOptionFails = 0
    private var lastQuoteFail: Long = 0
    private var lastOptionStatsFail: Long = 0

    // Updater Workers
    private var quoteUpdateWorker: Job? = null
    private var optionUpdateWorker: Job? = null


    init {
        loadSentinelSettings()
        loadBigListOfStocks()
    }


    /**
     * Functions
     */


    /**
     * Returns the current ticker the QuoteViewModel has loaded data for.
     */
    fun getCurrentTicker(): String{
        return curTicker
    }


    /**
     * Resets the current Quote + Option data values to null (triggering animation),
     * Launches new Updater Workers cancelling any previous workers if they exist.
     */
    fun setCurrentTicker(ticker: String){

        // Reset current quote data to null
        _stockQuote.value = null
        _etfQuote.value = null
        _optionStats.value = null

        // Reset current fails to Zero
        curQuoteFails = 0
        curOptionFails = 0
        _quoteFailed.value = false
        _optionStatsFailed.value = false

        // Set ticker to requested symbol
        curTicker = ticker

        // Start new updaters
        if (ticker != ""){
            startQuoteUpdater()
            startOptionStatsUpdater()

            Log.d("INFO", "$locTAG.setCurrentTicker has updated ticker " +
                    "to $ticker And started new Updaters")
        }

    }


    /**
     * Loads list of stocks from csv and updates the _bigStockList stateflow
     */
    private fun loadBigListOfStocks(){

        viewModelScope.launch(Dispatchers.IO){
            val masterList = StockSymbolsLoader.loadSyms()

            val neededList = mutableListOf<List<String>>()

            for (s in masterList!!){
                neededList.add(listOf(s[0], s[1]))
            }

            stockSymbolsList = neededList
        }
    }


    /**
     * Searches bigStockList for matching search results. Updates _searchResultsV2
     */
    fun searchBigStockList(usrTxt: String){

        // Empty search text should clear the search
        if (usrTxt == ""){
            _searchMatchesV2.value = mutableListOf()
            return
        }

        //
        if (!this::stockSymbolsList.isInitialized){
            return
        }

        viewModelScope.launch(Dispatchers.IO){
            val matches = mutableListOf<List<String>>()


            for (s in stockSymbolsList) {
                val (stock, compName) = s

                if (stock.startsWith(usrTxt)) {
                    matches.add(s)
                }
            }

            _searchMatchesV2.value = matches
        }
    }


    /**
     * Clears the searchResults flow
     */
    fun clearSearchResults(){
        _searchMatchesV2.value = listOf()
    }


    /**
     * Attempts to fetch StockQuote. On success the quoteType is updated as well as the _stockQuote
     */
    private suspend fun stockQuoteCollector(): Boolean{

        var success = false
        repo.getStockQuote(curTicker).collect{quoteStatus ->
            when (quoteStatus){
                is Resource.Loading -> {
                    Log.d("INFO", "$locTAG.stockQuoteCollector received Loading for " +
                            "$curTicker StockQuote")
                }

                is Resource.Error -> {
                    Log.d("INFO", "$locTAG.stockQuoteCollector received Error for " +
                            "$curTicker StockQuote. Will try for EtfQuote instead")

                    // Update fail
                    if (System.currentTimeMillis() - lastQuoteFail < 60_000){
                        curQuoteFails += 1
                        if (curQuoteFails >= maxQuoteFails){
                            _optionStatsFailed.value = true
                            Log.d("INFO", "$locTAG.stockQuoteCollector curQuoteFails" +
                                    " has reached MAX FAILS.")
                        }
                        lastQuoteFail = System.currentTimeMillis()
                    }else{
                        // Reset to zero if it's been awhile
                        lastQuoteFail = System.currentTimeMillis()
                        curQuoteFails = 0
                    }

                }

                is Resource.Success -> {
                    Log.d("INFO", "$locTAG.stockQuoteCollector received Success for " +
                            "$curTicker StockQuote. quoteType will be set to 'STOCK'")
                    success = true
                    quoteType = "STOCK"
                    _stockQuote.value = quoteStatus.data
                }
            }
        }
        return success
    }


    /**
     * Attempts to fetch EtfQuote. On success the quoteType is updated as well as the _etfQuote
     */
    private suspend fun etfQuoteCollector(): Boolean{

        var success = false
        repo.getETFQuote(curTicker).collect{quoteStatus ->

            when (quoteStatus){

                is Resource.Loading -> {
                    Log.d("INFO", "$locTAG.etfQuoteCollector received Loading for " +
                            "$curTicker EtfQuote")
                }

                is Resource.Error -> {
                    Log.d("INFO", "$locTAG.etfQuoteCollector received Error for " +
                            "$curTicker EtfQuote.")

                    // Update fails
                    if (System.currentTimeMillis() - lastQuoteFail < 60_000){
                        curQuoteFails += 1
                        if (curQuoteFails >= maxQuoteFails){
                            _quoteFailed.value = true
                            Log.d("INFO", "$locTAG.etfQuoteCollector curQuoteFails has" +
                                    " reached MAX FAILS.")
                        }
                        lastQuoteFail = System.currentTimeMillis()
                    }else{
                        // Reset to zero if it's been awhile
                        lastQuoteFail = System.currentTimeMillis()
                        curQuoteFails = 0
                    }

                }

                is Resource.Success -> {
                    Log.d("INFO", "$locTAG.etfQuoteCollector received Success for " +
                            "$curTicker EtfQuote. quoteType will be set to 'ETF'")
                    success = true
                    quoteType = "ETF"
                    _etfQuote.value = quoteStatus.data
                }
            }
        }
        return success
    }


    /**
     * Attempts to fetch OptionStats. On success the _optionStats is updated
     */
    private suspend fun optionStatsCollector(): Boolean{

        var success = false
        repo.getOptionStats(curTicker).collect{optionStatsRequest ->
            when (optionStatsRequest){
                is Resource.Loading -> {
                    Log.d("INFO", "$locTAG.optionStatsCollector received Loading for " +
                            "$curTicker OptionStats")
                }

                is Resource.Error -> {
                    Log.d("INFO", "$locTAG.optionStatsCollector received Error for " +
                            "$curTicker OptionStats. Will try for OptionStats instead")

                    // Update fail
                    if (System.currentTimeMillis() - lastOptionStatsFail < 60_000){
                        curOptionFails += 1
                        if (curOptionFails >= maxOptionFails){
                            _optionStatsFailed.value = true
                            Log.d("INFO", "$locTAG.optionStatsCollector curOptionFails" +
                                    " has reached MAX FAILS.")
                        }
                        lastOptionStatsFail = System.currentTimeMillis()
                    }else{
                        // Reset to zero if it's been awhile since the last fail
                        lastOptionStatsFail = System.currentTimeMillis()
                        curOptionFails = 0
                    }

                }

                is Resource.Success -> {
                    Log.d("INFO", "$locTAG.optionStatsCollector received Success for " +
                            "$curTicker OptionStats.")
                    success = true
                    _optionStats.value = optionStatsRequest.data
                }
            }
        }
        return success
    }


    /**
     * Can be called any time to Cancel & Join previous worker, then immediately start the new worker.
     * Resets the quoteType when Cancel & Join happens.
     */
    private fun startQuoteUpdater(){

        // Starts using coroutine so that previous workers can be canceled
        viewModelScope.launch(Dispatchers.IO){
            // Check for & Cancel previous UpdateWorker. Resets quote type
            if (quoteUpdateWorker?.isActive == true){
                quoteUpdateWorker!!.cancelAndJoin()
                quoteType = ""
                Log.d("INFO", "$locTAG.startQuoteUpdaters canceled previous worker and" +
                        " reset quoteType.")
            }

            // Start new worker that check quote type on each interval
            quoteUpdateWorker = periodicCoroutineRepeatOnFailure(
                delayTime = quoteUpdateInterval,
                scope = viewModelScope,
                dispatcher = Dispatchers.IO
            ){

                if (!_quoteFailed.value){

                    var success = false

                    // Determine quote type and fetch
                    when (quoteType){

                        "STOCK" -> {
                            success = stockQuoteCollector()
                        }

                        "ETF" -> {
                            success = etfQuoteCollector()
                        }

                        "" -> {

                            // Stock quote first
                            success = stockQuoteCollector()

                            // Etf quote if Stock Quote failed.
                            if (!success){
                                success = etfQuoteCollector()
                            }
                        }
                    }

                    // Success determines if it should be immediately retried, or wait till interval
                    success

                }else{
                    Log.d("INFO", "$locTAG.startQuoteUpdater Max Fails Reached for " +
                            "quote updates. Not Fetching Quote!")
                    true
                }
            }
        }
    }


    /**
     * Can be called any time to Cancel & Join previous worker, then immediately start the new worker.
     *
     */
    private fun startOptionStatsUpdater(){

        // Cancel and Join previous worker if active
        viewModelScope.launch(Dispatchers.IO){

            if (optionUpdateWorker?.isActive == true){
                optionUpdateWorker!!.cancelAndJoin()
                Log.d("INFO", "$locTAG.startOptionStatsUpdater has canceled " +
                        "previous worker.")
            }

            // Start new worker
            optionUpdateWorker = periodicCoroutineRepeatOnFailure(
                delayTime = optionUpdateInterval,
                scope = viewModelScope,
                dispatcher = Dispatchers.IO,
            ){
                if (!_optionStatsFailed.value){
                    optionStatsCollector()
                }else{
                    Log.d("INFO", "$locTAG.startOptionStatsUpdater Max Fails Reached " +
                            "for option stats updates. Not Fetching Option Stats!")
                    true
                }
            }
        }

    }


    /**
     * Loads the sentinel settings to _sentinelSettings
     */
    private fun loadSentinelSettings(){
        viewModelScope.launch(Dispatchers.IO){
            prefs.getSentinelSettings().collect{
                _sentinelSettings.value = it
            }
        }
    }


    /**
     * Updates sentinel settings in Datastore + Resets the _sentinelSettings
     */
    private fun saveSentinelSettings(settings: SentinelSettings){
        viewModelScope.launch(Dispatchers.IO){
            prefs.updateSentinelSettings(settings)
        }
        _sentinelSettings.value = settings
    }


    /**
     * Adds ticker to sentinel settings
     */
    fun addTickerToSentinelSettingsWatchlist(ticker: String){
        if (_sentinelSettings.value == null){return}

        if (_sentinelSettings.value?.currentWatchlist?.contains(ticker) == true){return}

        val settings = _sentinelSettings.value!!

        settings.currentWatchlist.add(ticker)

        saveSentinelSettings(settings)

        Log.d("INFO", "$locTAG.addTickerToSentinelSettingsWatchlist called. " +
                "Ticker Added: $ticker")
        loadSentinelSettings()
    }


    /**
     * Removes ticker from sentinel settings
     */
    fun removeTickerFromSentinelSettingWatchlist(ticker: String){
        if (_sentinelSettings.value == null){return}

        if (_sentinelSettings.value?.currentWatchlist?.contains(ticker) == false){return}

        val settings = _sentinelSettings.value!!

        settings.currentWatchlist.remove(ticker)

        saveSentinelSettings(settings)

        loadSentinelSettings()

        Log.d("INFO", "$locTAG.removeTickerFromSentinelSettingsWatchlist called. " +
                "Ticker Removed: $ticker")
    }

}
