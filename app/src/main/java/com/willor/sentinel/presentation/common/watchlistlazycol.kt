package com.willor.sentinel.presentation.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.ktstockdata.watchlistsdata.dataobjects.Watchlist
import com.willor.sentinel.ui.theme.Sizes
import kotlinx.coroutines.launch


@Composable
fun WatchlistLazyCol(
    navigator: DestinationsNavigator,
    watchlist: Watchlist,
    watchlistCardOnClick: (ticker: String) -> Unit,
    watchlistCardAddOnClick: (ticker: String) -> Unit
){
    // Location tag and states
    val locTAG = "WatchlistDisplay"
    val colScrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val lastListName = remember { mutableStateOf("null")}

    // Lazy column Container
    Column(
        modifier = Modifier
            .fillMaxHeight(.75f)
            .fillMaxWidth()
            .padding(Sizes.HORIZONTAL_EDGE_PADDING, 0.dp)
    ){

        // Lazy Column to display WatchlistTickerDisplayCard
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(Sizes.HORIZONTAL_PADDING_SMALL, Sizes.VERTICAL_PADDING_SMALL),
            verticalArrangement = Arrangement.spacedBy(Sizes.CONTENT_SPACER_LARGE),
            state = colScrollState,
        ){

            items(watchlist.tickers.size){index ->

                WatchlistItemCard(ticker = watchlist.tickers[index],
                    onCardClicked = {
                        Log.d("INFO","$locTAG Watchlist Card Clicked for Ticker: $it")
                        watchlistCardOnClick(it)
                    },
                    onAddButtonClicked = {
                        Log.d("INFO","$locTAG Watchlist Card Add button clicked for: $it")
                        watchlistCardAddOnClick(it)
                    }
                )

            }
        }

        SideEffect {
            coroutineScope.launch {
                if (watchlist.name != lastListName.value){
                    Log.d("INFO","$locTAG SideEffect Launched...Reset LazyCol position.")
                    lastListName.value = watchlist.name
                    colScrollState.scrollToItem(0)
                }
            }
        }
    }
}
