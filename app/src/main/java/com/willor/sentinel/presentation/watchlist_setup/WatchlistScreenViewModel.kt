package com.willor.sentinel.presentation.watchlist_setup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class WatchlistScreenViewModel @Inject constructor(

): ViewModel(){

    val locTAG = WatchlistScreenViewModel::class.simpleName
}
