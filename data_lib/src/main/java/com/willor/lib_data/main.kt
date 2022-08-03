package com.willor.lib_data

import com.willor.ktstockdata.watchlists_data.Watchlists


fun main() {
    val watchlists = Watchlists()

    println(
        watchlists.searchForWatchlistByKeywords("BIG", "EARNINGS")
    )
}