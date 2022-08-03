package com.willor.lib_data.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.willor.ktstockdata.quote_data.dataobjects.StockQuote
import com.willor.lib_data.utils.DbConstants
import java.util.*


fun StockQuote.toStockQuoteEntity(): StockQuoteEntity{
    return StockQuoteEntity(
        ticker, System.currentTimeMillis(), prevClose, openPrice, bidPrice, bidSize, askPrice,
        askSize, daysRangeHigh, daysRangeLow, fiftyTwoWeekRangeHigh, fiftyTwoWeekRangeLow, volume,
        avgVolume, marketCap, betaFiveYearMonthly, peRatioTTM, epsTTM, nextEarningsDate, 
        forwardDivYieldValue, forwardDivYieldPercentage, exDividendDate, oneYearTargetEstimate
    )
}


fun StockQuoteEntity.toStockQuote(): StockQuote{
    return StockQuote(
        ticker, prevClose!!, openPrice!!, bidPrice!!, bidSize!!, askPrice!!, askSize!!, daysRangeHigh!!, 
        daysRangeLow!!, fiftyTwoWeekRangeHigh!!, fiftyTwoWeekRangeLow!!, volume!!, avgVolume!!, marketCap!!, 
        betaFiveYearMonthly!!, peRatioTTM!!, epsTTM!!, nextEarningsDate!!, forwardDivYieldValue!!, 
        forwardDivYieldPercentage!!, exDividendDate!!, oneYearTargetEstimate!!,
    )
}


@Entity(tableName = DbConstants.STOCK_QUOTE_TABLE)
data class StockQuoteEntity(
    @PrimaryKey val ticker: String = "",
    @ColumnInfo val timeSaved: Long = System.currentTimeMillis(),
    @ColumnInfo val prevClose: Double? = null,
    @ColumnInfo val openPrice: Double? = null,
    @ColumnInfo val bidPrice: Double? = null,
    @ColumnInfo val bidSize: Int? = null,
    @ColumnInfo val askPrice: Double? = null,
    @ColumnInfo val askSize: Int? = null,
    @ColumnInfo val daysRangeHigh: Double? = null,
    @ColumnInfo val daysRangeLow: Double? = null,
    @ColumnInfo val fiftyTwoWeekRangeHigh: Double? = null,
    @ColumnInfo val fiftyTwoWeekRangeLow: Double? = null,
    @ColumnInfo val volume: Int? = null,
    @ColumnInfo val avgVolume: Int? = null,
    @ColumnInfo val marketCap: Long? = null,
    @ColumnInfo val betaFiveYearMonthly: Double? = null,
    @ColumnInfo val peRatioTTM: Double? = null,
    @ColumnInfo val epsTTM: Double? = null,
    @ColumnInfo val nextEarningsDate: Date? = null,
    @ColumnInfo val forwardDivYieldValue: Double? = null,
    @ColumnInfo val forwardDivYieldPercentage: Double? = null,
    @ColumnInfo val exDividendDate: Date? = null,
    @ColumnInfo val oneYearTargetEstimate: Double? = null,
)