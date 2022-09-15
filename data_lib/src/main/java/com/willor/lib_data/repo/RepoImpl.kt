package com.willor.lib_data.repo

import android.util.Log
import com.willor.ktstockdata.historicchartdata.charts.advancedchart.AdvancedStockChart
import com.willor.ktstockdata.historicchartdata.charts.simplechart.SimpleStockChart
import com.willor.ktstockdata.marketdata.dataobjects.*
import com.willor.ktstockdata.watchlistsdata.WatchlistOptions
import com.willor.ktstockdata.watchlistsdata.dataobjects.Watchlist
import com.willor.lib_data.data.local.db.StockDataDb
import com.willor.lib_data.domain.abstraction.IRepo
import com.willor.lib_data.domain.abstraction.IStockData
import com.willor.lib_data.domain.abstraction.Resource
import com.willor.lib_data.domain.models.*
import com.willor.sentinelscanners.domain.models.TriggerBase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RepoImpl @Inject constructor(
    private val db: StockDataDb,
    private val api: IStockData) : IRepo {

    private val locTAG = "RepoImpl"

    private val quoteTimeout: Long = 10_000
    private val chartTimeout: Long = 10_000
    private val watchlistTimeout: Long = 10_000
    private val miscDataTimeout: Long = 10_000


    /**
     * Takes the timeSaved and timeout arguments and checks them as...
     *
     * if: (System.currentTimeInMillis - timeSaved > refreshTimeout) : Fetch new data...
     * else: return cached data
     */
    private fun checkIfTimeToRefresh(objSaveTime: Long, timeout: Long): Boolean{
        if (System.currentTimeMillis() - objSaveTime > timeout){
            return true
        }
        return false
    }


    override fun getETFQuote(ticker: String): Flow<Resource<EtfQuote>> {

        return flow{

            emit(Resource.Loading())

            // Flag to fetch fresh data
            var refreshData = true

            // Check DB for cached data
            val dbData = db.getETFQuoteDAO().getByTicker(ticker)
            if (dbData != null){

                // Check if cached data is still valid
                if (!checkIfTimeToRefresh(dbData.timeSaved, quoteTimeout)){
                    refreshData = false
                }
            }

            // Fetch new data if needed
            if (refreshData){
                val data = api.getEtfQuote(ticker)

                // Verify data
                if (data != null){

                    // Save data to cache
                    db.getETFQuoteDAO().insertAllIntoTable(data.toETFQuoteEntity())
                    Log.d("INFO", "$ticker ETFQuote fetched and saved")

                    // Emit Success with data
                    emit(Resource.Success(data))
                }

                // Emit Error signaling failure to fetch data
                else{
                    emit(Resource.Error("Failed to fetch ETFQuote for $ticker"))
                }
            }

            // Return cached data
            else {
                emit(Resource.Success(dbData!!.toETFQuote()))
            }
        }
    }


    override fun getStockQuote(ticker: String): Flow<Resource<StockQuote>> {
        val checkForCachedData = {
            var result: StockQuote? = null
            val cachedData = db.getStockQuoteTableDAO().getByTicker(ticker)
            if (cachedData != null){
                if (!checkIfTimeToRefresh(cachedData.timeSaved, quoteTimeout)){
                    result = cachedData.toStockQuote()
                }
            }
            else{
                result = null
            }
            result
        }

        val fetchNewData = {

            var result: StockQuote? = null

            var data = api.getStockQuote(ticker)

            // Save to db if not null, and set as lambda result
            if (data != null){
                // Save to db
                db.getStockQuoteTableDAO().insertAllIntoTable(data.toStockQuoteEntity())

                result = data
            }

            result
        }

        return flow{
            emit(Resource.Loading())

            var data = checkForCachedData()

            // Check for null / fetch new data
            if (data == null){
                data = fetchNewData()
            }

            // Double check null (for new data) + Return
            if (data != null){
                emit(Resource.Success(data))
            }
            // Emit ERROR for null return
            else{
                emit(Resource.Error("Failed to acquire StockQuote for $ticker..." +
                        "Maybe it's an ETF?"))
            }
        }

    }


    override fun getOptionStats(ticker: String): Flow<Resource<OptionStats>> {

        val checkCache = {
            var result: OptionStats? = null

            val cacheData = db.getOptionStatsTableDAO().getByTicker(ticker)

            if (cacheData != null){
                if (!checkIfTimeToRefresh(cacheData.timeSaved, quoteTimeout)){
                    result = cacheData.toOptionStats()
                }
            }

            result
        }

        val fetchNewData = {

            val data = api.getOptionStats(ticker)

            if (data != null){
                db.getOptionStatsTableDAO().insertAllIntoTable(data.toOptionStatsEntity())
            }
            data
        }

        return flow{
            emit(Resource.Loading())

            var data: OptionStats?

            data = checkCache()

            if (data == null){
                data = fetchNewData()
            }

            // Check if data was obtained and emit success
            if (data != null){
                emit(Resource.Success(data))
            }
            // Emit Error on failure
            else{
                emit(Resource.Error("Failed to acquire OptionStats for $ticker"))
            }
        }
    }


    override fun getFuturesData(): Flow<Resource<MajorFuturesData>> {
        val checkCache = {
            var result: MajorFuturesData? = null
            val cachedData = db.getMajorFuturesDataDAO().getAllFromTable()

            // Verify cached data != null and is not timed out
            if (!cachedData.isNullOrEmpty()){
                val data = cachedData[cachedData.lastIndex]
                if (!checkIfTimeToRefresh(data.timeSaved, miscDataTimeout)){
                    result = data.toMajorFuturesData()
                }
            }
            result
        }

        val fetchNewData = {
            val data = api.getFuturesData()
            if (data != null){
                db.getMajorFuturesDataDAO().insertAllIntoTable(data.toMajorFuturesDataEntity())
            }
            data
        }

        return flow{
            emit(Resource.Loading())

            var data = checkCache()

            if (data == null){
                data = fetchNewData()
            }

            if (data != null){
                emit(Resource.Success(data))
            }

            else{
                emit(Resource.Error())
            }

        }
    }


    override fun getMajorIndicesData(): Flow<Resource<MajorIndicesData>> {
        val checkCache = {
            var result: MajorIndicesData? = null

            val cachedData = db.getMajorIndicesDataDAO().getAllFromTable()
            if (!cachedData.isNullOrEmpty()){
                val data = cachedData[cachedData.lastIndex]
                if (!checkIfTimeToRefresh(data.timeSaved, miscDataTimeout)){
                    result = data.toMajorIndicesData()
                }
            }
            result
        }

        val fetchNewData = {
            val data = api.getMajorIndicesData()
            if (data != null){
                db.getMajorIndicesDataDAO().insertAllIntoTable(data.toMajorIndexDataEntity())
            }
            data
        }

        return flow{
            emit(Resource.Loading())

            var data = checkCache()

            if (data == null){
                data = fetchNewData()
            }

            if (data != null){
                emit(Resource.Success(data))
            }

            else{
                emit(Resource.Error("Failed to acquire MajorIndicesData"))
            }
        }
    }


    override fun getSupportAndResistance(ticker: String): Flow<Resource<SnRLevels>> {
        val checkCache = {
            var result: SnRLevels? = null
            val cacheData = db.getSnRLevelsTableDAO().getByTicker(ticker)

            if (cacheData != null){
                if (!checkIfTimeToRefresh(cacheData.timeSaved, miscDataTimeout)){
                    result = cacheData.toSnRLevels()
                }
            }
            result
        }

        val fetchNewData = {
            val data = api.getSupportAndResistance(ticker)
            if (data != null){
                db.getSnRLevelsTableDAO().insertAllIntoTable(data.toSnRLevelsEntity())
            }
            data
        }

        return flow{
            emit(Resource.Loading())

            var data = checkCache()

            if (data == null){
                data = fetchNewData()
            }

            if (data != null){
                emit(Resource.Success(data))
            }

            else{
                emit(Resource.Error("Failed to acquire SnRLevels for $ticker"))
            }
        }
    }


    override fun getWatchlist(w: WatchlistOptions): Flow<Resource<Watchlist>> {
        val checkCache = {
            var result: Watchlist? = null

            val cachedData = db.getWatchlistTableDAO().getWatchlistByName(w.name)
            if (cachedData != null){
                if (!checkIfTimeToRefresh(cachedData.timeSaved, watchlistTimeout)){
                    result = cachedData.toWatchlist()
                }
            }

            result
        }

        val fetchNewData = {
            val data = api.getWatchlist(w)

            if (data != null){
                db.getWatchlistTableDAO().insertAllIntoTable(data.toWatchlistEntity())
            }

            data
        }

        return flow{
            emit(Resource.Loading())

            var data = checkCache()

            if (data == null){
                data = fetchNewData()
            }

            if (data != null){
                emit(Resource.Success(data))
            }

            else{
                emit(Resource.Error("Failed to acquire Watchlist named ${w.name}"))
            }
        }
    }


    override fun getHistoricDataAsAdvancedStockChart(
        ticker: String,
        interval: String,
        periodRange: String,
        prepost: Boolean): Flow<Resource<AdvancedStockChart>> {

        // Returns valid cached chart, else null
        val checkForCachedChart = {
            var result: AdvancedStockChart? = null
            var cachedChart: AdvChartEntity? = null
            val cachedCharts = db.getAdvChartDAO().getAllMatchingFromTable(
                ticker, interval, periodRange, prepost
            )

            // Get last chart in list
            if (!cachedCharts.isNullOrEmpty()){
                cachedChart = cachedCharts[cachedCharts.lastIndex]
            }

            if (cachedChart != null &&
                !checkIfTimeToRefresh(cachedChart.timeSaved, chartTimeout)){
                result = cachedChart.toAdvancedStockChart()
            }

            result
        }

        // Start flow
        return flow{

            emit(Resource.Loading())

            val cachedChart: AdvancedStockChart? = checkForCachedChart()

            // Check cache result, return if valid
            if (cachedChart != null){
                emit(Resource.Success(cachedChart))
            }

            // Fetch new chart, cache it, return it
            else{
                val newChart = api.getHistoryAsAdvancedStockChart(
                    ticker, interval, periodRange, prepost
                )

                if (newChart != null){

                    // Save it to cache
                    db.getAdvChartDAO().insertAllIntoTable(newChart.toAdvChartEntity())

                    // return
                    emit(Resource.Success(newChart))
                }

                else{
                    emit(Resource.Error("Failed to acquire chart for $ticker"))
                }
            }
        }
    }


    override fun getHistoricDataAsSimpleStockChart(
        ticker: String,
        interval: String,
        periodRange: String,
        prepost: Boolean): Flow<Resource<SimpleStockChart>> {

        // Returns valid cached chart, else null
        val checkForCachedChart = {
            var result: SimpleStockChart? = null
            var cachedChart: SimpleChartEntity? = null
            val cachedCharts = db.getSimpleChartTableDAO().getAllMatchingFromTable(
                ticker, interval, periodRange, prepost
            )

            // Pull most recent one from db
            if (!cachedCharts.isNullOrEmpty()){
                cachedChart = cachedCharts[cachedCharts.lastIndex]
            }

            if (cachedChart != null &&
                !checkIfTimeToRefresh(cachedChart.timeSaved, chartTimeout)){
                result = cachedChart.toSimpleStockChart()
            }
            result
        }

        return flow{
            emit(Resource.Loading())

            val cachedChart = checkForCachedChart()

            when{

                cachedChart != null ->{
                    emit(Resource.Success(cachedChart))
                }

                else ->{

                    val newChart = api.getHistoryAsSimpleStockChart(
                        ticker, interval, periodRange, prepost
                    )

                    if (newChart != null){
                        db.getSimpleChartTableDAO()
                            .insertAllIntoTable(newChart.toSimpleChartEntity())
                        emit(Resource.Success(newChart))
                    }
                    else{
                        emit(Resource.Error("Failed to acquire chart for $ticker"))
                    }
                }
            }
        }
    }


    override fun buildAndSaveTriggerEntity(
        trigger: TriggerBase,
    ): TriggerEntity {

        // Log the method call
        Log.d("INFO", "$locTAG.buildAndSaveTriggerEntity() Called for trigger: $trigger")

        // Build trigger entity
        val trigEntity = TriggerEntity(
            timestamp = System.currentTimeMillis(),
            ticker = trigger.ticker,
            triggerValue = trigger.triggerValue,
            rating = trigger.rating,
            strategyName = trigger.strategyName,
            strategyDescription = trigger.strategyDescription,
            priceOfAsset = trigger.priceOfAsset,
            volumeOfTriggerCandle = trigger.volumeOfTriggerCandle,
        )

        // Insert into db
        db.getTriggerTableDAO().insertAllIntoTable(
            trigEntity
        )

        // Return for display purposes
        return trigEntity
    }


    override suspend fun getAllTriggersWithinTimeRange(
        rangeStart: Long?,
        rangeEnd: Long?
    ): Flow<List<TriggerEntity>?> {

        // If null, end is current time
        val end: Long = rangeEnd ?: System.currentTimeMillis()

        // If null, start is 12hrs prior to end
        val start: Long = rangeStart ?: (end - (((60 * 60).toLong() * 12) * 1000))
        return db.getTriggerTableDAO().getAllFromTableInTimeRangeAsFlow(start, end)
    }
}
















