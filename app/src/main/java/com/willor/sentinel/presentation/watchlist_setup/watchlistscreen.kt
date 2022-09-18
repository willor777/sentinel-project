package com.willor.sentinel.presentation.watchlist_setup

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun WatchlistScreen(
    navigator: DestinationsNavigator,
    viewmodel: WatchlistScreenViewModel = hiltViewModel()
){

    Text("Watchlist Screen")


}
