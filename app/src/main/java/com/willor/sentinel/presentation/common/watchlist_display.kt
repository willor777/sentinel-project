package com.willor.sentinel.presentation.dash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.ktstockdata.watchlists_data.dataobjects.Ticker
import com.willor.ktstockdata.watchlists_data.dataobjects.Watchlist
import com.willor.lib_data.utils.printToDEBUGTEMP
import com.willor.sentinel.presentation.common.WatchlistTickerDisplayCard
import com.willor.sentinel.ui.theme.Sizes


@Composable
fun WatchlistDisplay(
    navigator: DestinationsNavigator,
    watchlist: Watchlist,
    watchlistCardOnClick: (ticker: String) -> Unit,
    watchlistCardAddOnClick: (ticker: String) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxHeight(.75f)
            .fillMaxWidth()
            .padding(Sizes.HORIZONTAL_EDGE_PADDING, 0.dp)
            .background(MaterialTheme.colorScheme.tertiary)
    ){
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(Sizes.HORIZONTAL_PADDING_SMALL, Sizes.VERTICAL_PADDING_SMALL),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ){

            items(watchlist.tickers.size){index ->

                WatchlistTickerDisplayCard(ticker = watchlist.tickers[index],
                    onCardClicked = {
                        printToDEBUGTEMP("Watchlist Card Clicked for Ticker: $it")
                        watchlistCardOnClick(it)
                    },
                    onAddButtonClicked = {
                        printToDEBUGTEMP("Watchlist Card Add button clicked for: $it")
                        watchlistCardAddOnClick(it)
                    }
                )

            }
        }
    }
}