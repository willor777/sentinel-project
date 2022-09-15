package com.willor.lib_data.domain.abstraction

import com.willor.ktstockdata.historicchartdata.charts.advancedchart.AdvancedStockChart
import com.willor.ktstockdata.historicchartdata.charts.simplechart.SimpleStockChart
import com.willor.ktstockdata.marketdata.dataobjects.*
import com.willor.ktstockdata.watchlistsdata.WatchlistOptions
import com.willor.ktstockdata.watchlistsdata.dataobjects.Watchlist


// TODO Add Watch lists
interface IStockData {

    fun getStockQuote(ticker: String): StockQuote?

    fun getEtfQuote(ticker: String): EtfQuote?

    fun getOptionStats(ticker: String): OptionStats?

    fun getFuturesData(): MajorFuturesData?

    fun getMajorIndicesData(): MajorIndicesData?

    fun getSupportAndResistance(ticker: String): SnRLevels?

    fun getWatchlist(w: WatchlistOptions): Watchlist?

    fun getHistoryAsAdvancedStockChart(
        ticker: String,
        interval: String = "5m",
        periodRange: String = "3d",
        prepost: Boolean = true
    ): AdvancedStockChart?

    fun getHistoryAsSimpleStockChart(
        ticker: String,
        interval: String = "5m",
        periodRange: String = "3d",
        prepost: Boolean = true
    ): SimpleStockChart?



}