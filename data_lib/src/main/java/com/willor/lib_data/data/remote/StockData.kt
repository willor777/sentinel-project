package com.willor.lib_data.data.remote

import com.willor.ktstockdata.historical_data.History
import com.willor.ktstockdata.historical_data.charts.advancedchart.AdvancedStockChart
import com.willor.ktstockdata.historical_data.charts.simplechart.SimpleStockChart
import com.willor.ktstockdata.misc_data.MiscData
import com.willor.ktstockdata.misc_data.dataobjects.MajorFuturesData
import com.willor.ktstockdata.misc_data.dataobjects.MajorIndicesData
import com.willor.ktstockdata.misc_data.dataobjects.SnRLevels
import com.willor.ktstockdata.quote_data.Quotes
import com.willor.ktstockdata.quote_data.dataobjects.ETFQuote
import com.willor.ktstockdata.quote_data.dataobjects.OptionStats
import com.willor.ktstockdata.quote_data.dataobjects.StockQuote
import com.willor.ktstockdata.watchlists_data.WatchlistNames
import com.willor.ktstockdata.watchlists_data.Watchlists
import com.willor.ktstockdata.watchlists_data.dataobjects.Watchlist
import com.willor.lib_data.domain.abstraction.IStockData


class StockData: IStockData {

    private val quotesApi by lazy{
        Quotes()
    }

    private val historyApi by lazy{
        History()
    }

    private val miscDataApi by lazy{
        MiscData()
    }

    private val watchlistApi by lazy{
        Watchlists()
    }


    override fun getStockQuote(ticker: String): StockQuote?{
        return quotesApi.getStockQuote(ticker)
    }


    override fun getEtfQuote(ticker: String): ETFQuote? {
        return quotesApi.getETFQuote(ticker)
    }


    override fun getOptionStats(ticker: String): OptionStats? {
        return quotesApi.getOptionStats(ticker)
    }


    override fun getFuturesData(): MajorFuturesData? {
        return miscDataApi.getFuturesData()
    }


    override fun getMajorIndicesData(): MajorIndicesData? {
        return miscDataApi.getMajorIndicesData()
    }


    override fun getSupportAndResistance(ticker: String): SnRLevels? {
        return miscDataApi.getSupportAndResistanceFromBarchartDotCom(ticker)
    }


    override fun getWatchlist(w: WatchlistNames): Watchlist? {
        return watchlistApi.getWatchlist(w)
    }


    override fun getHistoryAsAdvancedStockChart(
        ticker: String,
        interval: String,
        periodRange: String,
        prepost: Boolean
    ): AdvancedStockChart? {
        return historyApi.getHistoryAsAdvancedStockChart(
            ticker, interval, periodRange, prepost
        )
    }


    override fun getHistoryAsSimpleStockChart(
        ticker: String,
        interval: String,
        periodRange: String,
        prepost: Boolean
    ): SimpleStockChart? {
        return historyApi.getHistoryAsSimpleStockChart(
            ticker, interval, periodRange, prepost
        )
    }
}