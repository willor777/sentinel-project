package com.willor.sentinel.presentation.sentinel

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.lib_data.data.local.preferences.SentinelSettings
import com.willor.lib_data.domain.models.TriggerEntity
import com.willor.sentinel.presentation.common.AnimatedLoadingWidget
import com.willor.sentinel.presentation.common.SentinelWatchlistLazyRow
import com.willor.sentinel.presentation.common.TriggerLazyCol
import com.willor.sentinel.presentation.destinations.DashboardScreenDestination
import com.willor.sentinel.presentation.sentinel.components.SentinelActiveTopAppBar
import com.willor.sentinel.presentation.sentinel.components.SentinelBottomAppBar
import com.willor.sentinel.presentation.sentinel.components.SentinelInfoDisplay
import com.willor.sentinel.ui.theme.Sizes


@Destination
@Composable
fun SentinelActiveScreen(
    navigator: DestinationsNavigator,
    viewmodel: SentinelViewModel = hiltViewModel()
){
    viewmodel.startSentinelScanner(LocalContext.current)

    val sentinelSettings = viewmodel.sentinelSettings.collectAsState()

    val triggers = viewmodel.recentTriggersStateFlow.collectAsState()

    val showContent = remember { mutableStateOf(false) }

    AnimatedLoadingWidget(
        conditionCheck = {
            sentinelSettings.value != null
        },
        onConditionSuccess = {
            showContent.value = true
        }
    ){
        Text("Loading...")
    }

    SentinelActiveContent(
        show = showContent.value,
        sentinelSettings = sentinelSettings,
        triggersList = triggers,
        homeButtonOnClick = {navigator.navigate(DashboardScreenDestination)},
        settingsButtonOnClick = { /*TODO*/ },
        triggerCardOnClick = { Log.d("INFO", "Trigger Card Clicked. Ticker: $it") }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentinelActiveContent(
    show: Boolean,
    sentinelSettings: State<SentinelSettings?>,
    triggersList: State<List<TriggerEntity>>,
    homeButtonOnClick: () -> Unit = {},
    settingsButtonOnClick: () -> Unit = {},
    triggerCardOnClick: (ticker: String) -> Unit = {},
){

    if (show){

        val scaffoldState = rememberScaffoldState(
            rememberDrawerState(DrawerValue.Closed)
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,

            // Top Bar
            topBar = {
                SentinelActiveTopAppBar(
                    homeButtonOnClick = { homeButtonOnClick() },
                    settingsButtonOnClick = { settingsButtonOnClick() }
                )
            },

            // Bottom Bar
            bottomBar = {
                SentinelBottomAppBar(
                    editWatchlistOnClick = { /*TODO*/ }
                )            },

            // Drawer Content
            drawerContent = {
                // TODO Add drawer
            },
        ){

            // Main Content Layout...
            Column(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_EDGE_PADDING)
            ){


                // Scanner Info
                SentinelInfoDisplay(sentinelSettings = sentinelSettings)

                Spacer(Modifier.height(Sizes.CONTENT_SPACER_NORMAL))

                // Trigger display column
                TriggerLazyCol(
                    listOfTriggers = triggersList.value,
                    onCardClicked = {ticker -> triggerCardOnClick(ticker)}
                )

                Spacer(Modifier.height(Sizes.CONTENT_SPACER_NORMAL))


                SentinelWatchlistLazyRow(
                    tickerList = sentinelSettings.value?.currentWatchlist ?: listOf(),
                    onTickerClicked = {
                        // TODO
                    },
                    onRemoveTickerClicked = {
                        // TODO
                    }
                )


            }
        }
    }
}




