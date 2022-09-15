package com.willor.lib_data.data.remote

import com.willor.ktstockdata.KtStocks
import com.willor.ktstockdata.historicchartdata.charts.advancedchart.AdvancedStockChart
import com.willor.ktstockdata.historicchartdata.charts.simplechart.SimpleStockChart
import com.willor.ktstockdata.marketdata.MarketData
import com.willor.ktstockdata.marketdata.dataobjects.*
import com.willor.ktstockdata.watchlistsdata.WatchlistOptions
import com.willor.ktstockdata.watchlistsdata.dataobjects.Watchlist
import com.willor.lib_data.domain.abstraction.IStockData


class StockData: IStockData {

    private val ktstocks = KtStocks()

    private val marketDataApi = ktstocks.marketData

    private val historyApi = ktstocks.historicChartData

    private val watchlistApi = ktstocks.watchlistData


    override fun getStockQuote(ticker: String): StockQuote?{
        return marketDataApi.getStockQuote(ticker)
    }


    override fun getEtfQuote(ticker: String): EtfQuote? {
        return marketDataApi.getETFQuote(ticker)
    }


    override fun getOptionStats(ticker: String): OptionStats? {
        return marketDataApi.getOptionStats(ticker)
    }


    override fun getFuturesData(): MajorFuturesData? {
        return marketDataApi.getFuturesData()
    }


    override fun getMajorIndicesData(): MajorIndicesData? {
        return marketDataApi.getMajorIndicesData()
    }


    override fun getSupportAndResistance(ticker: String): SnRLevels? {
        return marketDataApi.getSupportAndResistanceFromBarchartDotCom(ticker)
    }


    override fun getWatchlist(w: WatchlistOptions): Watchlist? {
        return watchlistApi.getWatchlistByWatchlistOption(w)
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