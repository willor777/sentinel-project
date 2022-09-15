package com.willor.lib_data.domain.abstraction

import com.willor.ktstockdata.historicchartdata.charts.advancedchart.AdvancedStockChart
import com.willor.ktstockdata.historicchartdata.charts.simplechart.SimpleStockChart
import com.willor.ktstockdata.marketdata.dataobjects.*
import com.willor.ktstockdata.watchlistsdata.WatchlistOptions
import com.willor.ktstockdata.watchlistsdata.dataobjects.Watchlist
import com.willor.lib_data.domain.models.TriggerEntity
import com.willor.sentinelscanners.domain.models.TriggerBase
import kotlinx.coroutines.flow.Flow

interface IRepo {

    fun getETFQuote(ticker: String): Flow<Resource<EtfQuote>>

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