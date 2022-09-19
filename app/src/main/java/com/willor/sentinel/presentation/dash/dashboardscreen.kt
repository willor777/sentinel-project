@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.willor.sentinel.presentation.dash

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.ktstockdata.marketdata.dataobjects.MajorFuturesData
import com.willor.ktstockdata.watchlistsdata.WatchlistOptions
import com.willor.ktstockdata.watchlistsdata.dataobjects.Watchlist
import com.willor.sentinel.presentation.common.AnimatedLoadingWidget
import com.willor.sentinel.presentation.common.SentinelWatchlistLazyRow
import com.willor.sentinel.presentation.common.WatchlistLazyCol
import com.willor.sentinel.presentation.dash.components.*
import com.willor.sentinel.presentation.destinations.QuoteScreenDestination
import com.willor.sentinel.presentation.destinations.SentinelActiveScreenDestination
import com.willor.sentinel.presentation.destinations.WatchlistScreenDestination
import com.willor.sentinel.ui.theme.Sizes


/**
 * Contains the Dashboard's "View Logic". Makes sure the data it displays is completely loaded
 * allowing the content to be DashboardScreenContent displayed
 */
@Destination
@Composable
fun DashboardScreen(
    navigator: DestinationsNavigator,
    viewModel: DashboardViewModel = hiltViewModel()
){

    val context = LocalContext.current

    val futures = viewModel.futuresDataFlow.collectAsState()

    val watchlist = viewModel.watchlistDataFlow.collectAsState()

    // Todo This isn't really needed. Only the Sentinel Watchlist is used.
    val preferences = viewModel.appPreferences.collectAsState()

    // TODO you should prolly check that this is loaded before showing dashboard content
    val curSentinelWatchlist = viewModel.curSentinelWatchlist.collectAsState()

    var showErrorDialog by remember { mutableStateOf(false)}

    var showContent by remember { mutableStateOf(false)}

    AnimatedLoadingWidget(
        conditionCheck = {
            // Check for data success
            futures.value != null && watchlist.value != null && preferences.value != null
        },
        onConditionSuccess = {
            showContent = true
        },
        maxTime = 20_000,
        onMaxTimeReached = {
            showErrorDialog = true
        }
    ) {
        Text("Loading...")
    }


    // If loading fails after 10s, shows an error dialog
    FailedToLoadToast(showErrorDialog)

    // If load is success, show content
    DashboardScreenContent(
        showing = showContent,
        navigator = navigator,
        futuresData = futures,
        watchlistData = watchlist,
        sentinelWatchlist = curSentinelWatchlist,
        btmbarEditWatchlistOnClick = {
            navigator.navigate(WatchlistScreenDestination)
        },
        btmBarStartSentinelOnClick = {
            navigator.navigate(SentinelActiveScreenDestination)
        },
        watchlistChipOnClick = {
            viewModel.changeYfWatchlist(it)
        },
        watchlistCardOnClick = {
            navigator.navigate(QuoteScreenDestination(it))
        },
        watchlistCardAddOnClick = {
            viewModel.addTickerToSentinelWatchlist(
                it,
                failCallBack = {
                    Toast.makeText(context, "$it is already on Sentinel Watchlist",
                    Toast.LENGTH_SHORT).show()
                }
            )
        },
        sentinelWatchlistCardOnClick = {
            navigator.navigate(QuoteScreenDestination(it))
        },
        sentinelWatchlistRemoveTickerOnClick = {
            viewModel.removeTickerFromSentinelWatchlist(it)
        }
    )
}


@Composable
fun DashboardScreenContent(
    showing: Boolean,
    navigator: DestinationsNavigator,
    futuresData: State<MajorFuturesData?>,
    watchlistData: State<Watchlist?>,
    sentinelWatchlist: State<List<String>?>,
    btmbarEditWatchlistOnClick: () -> Unit = {},
    btmBarStartSentinelOnClick: () -> Unit = {},
    watchlistChipOnClick: (WatchlistOptions) -> Unit = {},
    watchlistCardOnClick: (ticker: String) -> Unit = {},
    watchlistCardAddOnClick: (ticker: String) -> Unit = {},
    sentinelWatchlistCardOnClick: (ticker: String) -> Unit = {},
    sentinelWatchlistRemoveTickerOnClick: (ticker: String) -> Unit = {}
){
    if (showing){

        val scaffoldState = rememberScaffoldState(
            rememberDrawerState(DrawerValue.Closed)
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,

            // Top Bar
            topBar = {
                DashTopAppBar(
                    logoutOnClick = {
                        Log.d("INFO", "DashboardTopAppBar logout clicked")
                                    },
                    settingsOnClick = {
                        Log.d("INFO", "DashboardTopAppBar navIcon clicked")
                    }
                )

            },

            // Bottom Bar
            bottomBar = {
                // TODO Add bottom bar
                DashBottomAppBar(
                    editWatchlistOnClick = { btmbarEditWatchlistOnClick() },
                    startSentinelOnClick = { btmBarStartSentinelOnClick() }
                )
            },

            // Drawer Content
            drawerContent = {
                // TODO Add drawer
            },
        ){ paddingValues ->
            
            // Main Content Layout
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ){

                // Spacer to separate TopAppBar and Futures Chips
                Spacer(Modifier.height(Sizes.CONTENT_SPACER_LARGE))
                
                // Futures Display
                FuturesDisplay(majorFuturesData = futuresData.value!!)

                // Spacer to separate Watchlist selector chips from Futures display
                Spacer(Modifier.height(Sizes.CONTENT_SPACER_LARGE))

                // Watchlist selector chips
                WatchlistNameChip(
                    watchlistOptions = listOf(
                        WatchlistOptions.MOST_ACTIVE,
                        WatchlistOptions.GAINERS,
                        WatchlistOptions.LOSERS,
                        WatchlistOptions.LARGEST_FIFTY_TWO_WEEK_GAINS
                    )
                ){wl ->
                    watchlistChipOnClick(wl)
                }

                Spacer(Modifier.height(Sizes.CONTENT_SPACER_SMALL))

                WatchlistLazyCol(
                    navigator = navigator,
                    watchlist = watchlistData.value!!,
                    watchlistCardOnClick = {
                        watchlistCardOnClick(it)
                    },
                    watchlistCardAddOnClick = {
                        watchlistCardAddOnClick(it)
                    }
                )

                Spacer(Modifier.height(Sizes.CONTENT_SPACER_SMALL))

                SentinelWatchlistLazyRow(
                    tickerList = sentinelWatchlist.value!!,
                    onTickerClicked = {
                        sentinelWatchlistCardOnClick(it)
                    },
                    onRemoveTickerClicked = {
                        sentinelWatchlistRemoveTickerOnClick(it)
                    }
                )
            }
        }
    }
}
