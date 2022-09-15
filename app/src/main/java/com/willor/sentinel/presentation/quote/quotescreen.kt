@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.willor.sentinel.presentation.quote

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.ktstockdata.marketdata.dataobjects.EtfQuote
import com.willor.ktstockdata.marketdata.dataobjects.OptionStats
import com.willor.ktstockdata.marketdata.dataobjects.StockQuote
import com.willor.lib_data.data.local.preferences.SentinelSettings
import com.willor.sentinel.presentation.common.SentinelWatchlistLazyRow
import com.willor.sentinel.presentation.common.TickerSymbolSearchBar
import com.willor.sentinel.presentation.destinations.DashboardScreenDestination
import com.willor.sentinel.presentation.quote.components.OptionStatsDisplay
import com.willor.sentinel.presentation.quote.components.QuoteBaseStats
import com.willor.sentinel.presentation.quote.components.QuoteBottomAppBar
import com.willor.sentinel.presentation.quote.components.QuoteTopAppBar
import com.willor.sentinel.ui.theme.Sizes


// TODO Include a "?" next to some of the data labels so that a user can
//      click it and get an explanation of what the data means...
//      Beta 5y Monthly, MarketCap, NAV, PE Ratio, etc...
//      Do it for the options board too
//

// TODO If a quote / option data request fails X amount of times it should stop requesting it.
//  Then it should show a Funny "No Data Available" image + a button to retry (reset fails to 0)
//  just in case maybe the phone was out of network when the data was being requested
@Destination
@Composable
fun QuoteScreen(
    initialTickerSymbol: String,
    navigator: DestinationsNavigator,
    viewModel: QuoteViewModel = hiltViewModel()
){

    val locTAG = "QuoteScreen"

    Log.d("INFO", "Viewmodel cur ticker = ... ${viewModel.getCurrentTicker()}")

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

    // Set the ticker in viewModel for first time. This will also trigger updates
    if (viewModel.getCurrentTicker() == "NONE"){
        viewModel.setCurrentTicker(initialTickerSymbol)
    Log.d("INFO", "Quote curticker set to: $initialTickerSymbol")
    }

    QuoteScreenContent(
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


@Composable
private fun QuoteScreenContent(
    stockQuote: State<StockQuote?>,
    etfQuote: State<EtfQuote?>,
    quoteFailed: State<Boolean>,
    optionStats: State<OptionStats?>,
    optionStatsFailed: State<Boolean>,
    sentinelSettings: State<SentinelSettings?>,
    searchResults: State<List<List<String>>>,
    homeIconOnClick: () -> Unit,
    settingsIconOnClick: () -> Unit,
    onSearchTextChange: (searchTxt: String) -> Unit,
    onSearchClick: (tick: String) -> Unit,
    onSuggestionItemClicked: (tick: String) -> Unit,
    onRetryClicked: () -> Unit,
    addToWatchlistOnClick: (tick: String) -> Unit,
    sentinelTickerOnClick: (tick: String) -> Unit,
    sentinelTickerRemoveOnClick: (tick: String) -> Unit,

    ){

    val curTicker = stockQuote.value?.ticker ?: etfQuote.value?.ticker ?: ""

    // State of scaffold
    val scaffoldState = rememberScaffoldState(
        rememberDrawerState(DrawerValue.Closed)
    )

    // Base layout
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            QuoteTopAppBar(
                homeButtonOnClick = { homeIconOnClick() },
                settingsButtonOnClick = { settingsIconOnClick() }
            )
        },
        bottomBar = {
            QuoteBottomAppBar(
                addToWatchlistOnClick = { addToWatchlistOnClick(curTicker) }
            )
        }
    ){_ ->

        Column(
            Modifier.padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_EDGE_PADDING)
        ){

            TickerSymbolSearchBar(
                onTextChange = {
                   onSearchTextChange(it)
                },
                onSearchClick = {
                    onSearchClick(it)
                },
                onSuggestionItemClicked = {
                    onSuggestionItemClicked(it)
                },
                searchResults = searchResults
            )

            // Base stats of symbol
            QuoteBaseStats(
                stockQuote = stockQuote,
                etfQuote = etfQuote,
                quoteFailed = quoteFailed,
                onRetryClicked = {
                    onRetryClicked()
                }
            )

            Spacer(Modifier.height(20.dp))

            // Option stats of symbol
            OptionStatsDisplay(
                optionStats = optionStats,
                optionStatsFailed = optionStatsFailed,
                onRetryClicked = {
                    onRetryClicked()
                }
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ){

                // Sentinel Watchlist Display
                SentinelWatchlistLazyRow(
                    sentinelSettings = sentinelSettings,
                    onTickerClicked = {
                        Log.d("INFO", "SentinelWatchlistSimple onTickerClicked() -> $it")
                        sentinelTickerOnClick(it)
                  },
                    onRemoveTickerClicked = {sentinelTickerRemoveOnClick(it)}
                )
            }

        }
    }
}



