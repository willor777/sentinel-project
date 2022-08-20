package com.willor.lib_data.domain.abstraction

import com.willor.ktstockdata.historical_data.charts.advancedchart.AdvancedStockChart
import com.willor.ktstockdata.historical_data.charts.simplechart.SimpleStockChart
import com.willor.ktstockdata.misc_data.dataobjects.MajorFuturesData
import com.willor.ktstockdata.misc_data.dataobjects.MajorIndicesData
import com.willor.ktstockdata.misc_data.dataobjects.SnRLevels
import com.willor.ktstockdata.quote_data.dataobjects.ETFQuote
import com.willor.ktstockdata.quote_data.dataobjects.OptionStats
import com.willor.ktstockdata.quote_data.dataobjects.StockQuote
import com.willor.ktstockdata.watchlists_data.WatchlistOptions
import com.willor.ktstockdata.watchlists_data.dataobjects.Watchlist
import com.willor.lib_data.domain.models.TriggerEntity
import com.willor.sentinel_bots.domain.models.TriggerBase
import kotlinx.coroutines.flow.Flow

interface IRepo {

    fun getETFQuote(ticker: String): Flow<Resource<ETFQuote>>

    fun getStockQuote(ticker: String): Flow<Resource<StockQuote>>

    fun getOptionStats(ticker: String): Flow<Resource<OptionStats>>

    fun getFuturesData(): Flow<Resource<MajorFuturesData>>

    fun getMajorIndicesData(): Flow<Resource<MajorIndicesData>>

    fun getSupportAndResistance(ticker: String): Flow<Resource<SnRLevels>>

    fun getWatchlist(w: WatchlistOptions): Flow<Resource<Watchlist>>

    fun getHistoricDataAsAdvancedStockChart(
        ticker: String,
        interval: String = "5m",
        periodRange: String = "1d",
        prepost: Boolean = true): Flow<Resource<AdvancedStockChart>>

    fun getHistoricDataAsSimpleStockChart(
        ticker: String,
        interval: String = "5m",
        periodRange: String = "1d",
        prepost: Boolean = true): Flow<Resource<SimpleStockChart>>

    fun buildAndSaveTriggerEntity(
        trigger: TriggerBase,
    ): TriggerEntity


    /**
     * Returns a list of all triggers in the Database for given time range. Both rangeStart
     * and rangeEnd can be null, If so will return for last 24hrs. If rangeEnd is null, will set
     * rangeEnd to current time. If rangeStart is null, will set to rangeEnd - 24hrs
     */
    suspend fun getAllTriggersWithinTimeRange(
        rangeStart: Long?,
        rangeEnd: Long?
    ): Flow<List<TriggerEntity>?>
}