package com.willor.sentinel.presentation.quote

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.sentinel.presentation.destinations.DashboardScreenDestination


const val locTAG = "QuoteScreenV2"

@Destination
@Composable
fun QuoteScreen(
    initialTickerSymbol: String,
    navigator: DestinationsNavigator,
    viewModel: QuoteViewModel = hiltViewModel()
){

    Log.d("INFO", "$locTAG SCREEN Loaded")

    // Set the ticker in viewModel for first time. This will also trigger updates
    if (viewModel.getCurrentTicker() == "NONE"){
        viewModel.setCurrentTicker(initialTickerSymbol)
        Log.d("INFO",
            "$locTAG viewModel's curticker set to: $initialTickerSymbol")
    }

    /**
     * State holders
     */

    // Sentinel Settings
    val sentinelSettings = viewModel.sentinelSettings.collectAsState()

    // Stock quote
    val stockQuote = viewModel.stockQuoteFlow.collectAsState()

    // Etf quote
    val etfQuote = viewModel.etfQuoteFlow.collectAsState()

    // Failed to find Quote
    val quoteFail = viewModel.quoteFailed.collectAsState()

    // Failed to find Option Stats
    val optionStatsFail = viewModel.optionStatsFailed.collectAsState()

    // Option stats
    val optionStats = viewModel.optionStatsFlow.collectAsState()

    // Search results
    val searchResults = viewModel.searchResults.collectAsState()



    QuoteScreenV2ContentHolder(
        stockQuote = stockQuote,
        etfQuote = etfQuote,
        quoteFailed = quoteFail,
        optionStats = optionStats,
        optionStatsFailed = optionStatsFail,
        sentinelSettings = sentinelSettings,
        searchResults = searchResults,
        homeIconOnClick = {navigator.navigate(DashboardScreenDestination)},
        settingsIconOnClick = {/*TODO*/},
        onSearchTextChange = {usrText ->
            viewModel.searchBigStockList(usrText)
        },
        onSearchClick = {
            viewModel.setCurrentTicker(it)
            viewModel.clearSearchResults()
        },
        onSuggestionItemClicked = {
            viewModel.setCurrentTicker(it)
            viewModel.clearSearchResults()
        },
        onRetryClicked = {
            viewModel.setCurrentTicker(viewModel.getCurrentTicker())
        },
        addToWatchlistOnClick = {
            Log.d("INFO", "Bottom bar clicked... Ticker: $it")
            if (it != ""){viewModel.addTickerToSentinelSettingsWatchlist(it)}
        },
        sentinelTickerOnClick = {
            Log.d("INFO", "Sentinel Ticker Card Clicked")
            viewModel.setCurrentTicker(it)
        },
        sentinelTickerRemoveOnClick = {
            viewModel.removeTickerFromSentinelSettingWatchlist(it)
        },
    )
}
