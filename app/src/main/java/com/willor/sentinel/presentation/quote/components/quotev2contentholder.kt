package com.willor.sentinel.presentation.quote

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.willor.ktstockdata.marketdata.dataobjects.EtfQuote
import com.willor.ktstockdata.marketdata.dataobjects.OptionStats
import com.willor.ktstockdata.marketdata.dataobjects.StockQuote
import com.willor.lib_data.data.local.preferences.SentinelSettings
import com.willor.sentinel.presentation.common.AnimatedLoadingWidget
import com.willor.sentinel.presentation.common.SentinelWatchlistLazyRow
import com.willor.sentinel.presentation.common.TickerSymbolSearchBar
import com.willor.sentinel.presentation.quote.components.*
import com.willor.sentinel.ui.theme.Sizes


/**
 * The content of the QuoteScreen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuoteScreenV2ContentHolder(
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

    val scrollState = rememberScrollState()
    var scrollEnabled = false

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

        // Outer Container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.9f)
                .padding(
                    Sizes.HORIZONTAL_EDGE_PADDING,
                    Sizes.VERTICAL_EDGE_PADDING
                ),
            verticalArrangement = Arrangement.SpaceAround

        ){

            // Search bar
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

            // Spacer between SearchBar and Quote Content
            Spacer(modifier = Modifier.height(Sizes.CONTENT_SPACER_NORMAL))

            // if no quote data -> Show non-scroll col w/ searchbar + load anim
            if (stockQuote.value == null && etfQuote.value == null){

                LoadingDisplay()

            }

            // if quote data -> Show scroll col w/ searchbar + quote
            if (stockQuote.value != null || etfQuote.value != null){
                scrollEnabled = true
                QuoteDisplay(stockQuote, etfQuote, optionStats)
            }

            // Spacer between Quote Content and Sentinel Watchlist
            Spacer(modifier = Modifier.height(Sizes.CONTENT_SPACER_NORMAL))

            // Sentinel Watchlist Display
            SentinelWatchlistLazyRow(
                sentinelSettings = sentinelSettings,
                onTickerClicked = {
                    Log.d("INFO", "SentinelWatchlistSimple onTickerClicked() -> $it")
                    sentinelTickerOnClick(it)
                },
                onRemoveTickerClicked = {sentinelTickerRemoveOnClick(it)}
            )


        }       // Column End

    }       // Scaffold End
}


@Composable
private fun LoadingDisplay(

){

    // No padding is needed
    AnimatedLoadingWidget(
        conditionCheck = {
            false       // TODO
        },
        onConditionSuccess = {
            // TODO
        },
        maxTime = 10_000,
        onMaxTimeReached = {
            // TODO
        },
        infoDisplay = {
            Text("Quote Loading")
        }
    )
}


@Composable
private fun QuoteDisplay(
    stockQuote: State<StockQuote?>,
    etfQuote: State<EtfQuote?>,
    optionStats: State<OptionStats?>,
){

    val scrollState = rememberScrollState()

    // No padding is needed
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.8f)
            .verticalScroll(scrollState)
    ){
        if (stockQuote.value != null){
            StockQuoteDisplayV2(stockQuote = stockQuote)
        }
        else if (etfQuote.value != null){
            ETFQuoteDisplayV2(etfQuote = etfQuote)
        }

        if (optionStats.value != null){
            OptionStatsDisplayContentV2(optionStats = optionStats)
        }
    }
}
