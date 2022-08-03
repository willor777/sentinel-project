package com.willor.ktstockdata.watchlists_data.dataobjects

data class Watchlist(
    val name: String,
    val tickers: List<Ticker>
)
