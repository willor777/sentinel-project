package com.willor.lib_data.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.willor.lib_data.data.local.models.*
import com.willor.lib_data.data.local.utils.Converters
import com.willor.lib_data.utils.DbConstants


@Database(
    entities = [
    AdvChartEntity::class,
    ETFQuoteEntity::class,
    MajorFuturesDataEntity::class,
    MajorIndicesDataEntity::class,
    OptionStatsEntity::class,
    SimpleChartEntity::class,
    SnRLevelsEntity::class,
    StockQuoteEntity::class,
    WatchlistEntity::class
               ],
    version = DbConstants.DB_VERS,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class StockDataDb : RoomDatabase(){

    abstract fun getAdvChartDAO(): AdvChartTableDAO

    abstract fun getETFQuoteDAO(): ETFQuoteTableDAO

    abstract fun getMajorFuturesDataDAO(): MajorFuturesDataTableDAO

    abstract fun getMajorIndicesDataDAO(): MajorIndicesDataTableDAO

    abstract fun getOptionStatsTableDAO(): OptionStatsTableDao

    abstract fun getSimpleChartTableDAO(): SimpleChartTableDAO

    abstract fun getSnRLevelsTableDAO(): SnRLevelsTableDAO

    abstract fun getStockQuoteTableDAO(): StockQuoteTableDAO

    abstract fun getWatchlistTableDAO(): WatchlistTableDAO
}