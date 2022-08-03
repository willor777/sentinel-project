package com.willor.lib_data.domain.abstraction

import com.willor.ktstockdata.historical_data.charts.advancedchart.AdvancedStockChart
import com.willor.ktstockdata.historical_data.charts.simplechart.SimpleStockChart
import com.willor.ktstockdata.misc_data.dataobjects.MajorFuturesData
import com.willor.ktstockdata.misc_data.dataobjects.MajorIndicesData
import com.willor.ktstockdata.misc_data.dataobjects.SnRLevels
import com.willor.ktstockdata.quote_data.dataobjects.ETFQuote
import com.willor.ktstockdata.quote_data.dataobjects.OptionStats
import com.willor.ktstockdata.quote_data.dataobjects.StockQuote
import com.willor.ktstockdata.watchlists_data.WatchlistNames
import com.willor.ktstockdata.watchlists_data.dataobjects.Watchlist


// TODO Add Watch lists
interface IStockData {

    fun getStockQuote(ticker: String): StockQuote?

    fun getEtfQuote(ticker: String): ETFQuote?

    fun getOptionStats(ticker: String): OptionStats?

    fun getFuturesData(): MajorFuturesData?

    fun getMajorIndicesData(): MajorIndicesData?

    fun getSupportAndResistance(ticker: String): SnRLevels?

    fun getWatchlist(w: WatchlistNames): Watchlist?

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