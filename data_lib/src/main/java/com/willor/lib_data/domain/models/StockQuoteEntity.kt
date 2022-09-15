package com.willor.lib_data.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.willor.ktstockdata.marketdata.dataobjects.StockQuote
import com.willor.lib_data.utils.DbConstants
import java.util.*


fun StockQuote.toStockQuoteEntity(): StockQuoteEntity{
    return StockQuoteEntity(
        ticker, System.currentTimeMillis(), changeDollarRegMarket, changePctRegMarket, lastPriceRegMarket,
        prevClose, openPrice, bidPrice, bidSize, askPrice,
        askSize, daysRangeHigh, daysRangeLow, fiftyTwoWeekRangeHigh, fiftyTwoWeekRangeLow, volume,
        avgVolume, marketCap, betaFiveYearMonthly, peRatioTTM, epsTTM, nextEarningsDate, 
        forwardDivYieldValue, forwardDivYieldPercentage, exDividendDate, oneYearTargetEstimate,
        prepostPrice, prepostChangeDollar, prepostChangePct, marketCapAbbreviatedString
    )
}


fun StockQuoteEntity.toStockQuote(): StockQuote {
    return StockQuote(
        ticker, changeDollarToday!!, changePctToday!!, curPrice!!,
        prevClose!!, openPrice!!, bidPrice!!, bidSize!!, askPrice!!, askSize!!, daysRangeHigh!!,
        daysRangeLow!!, fiftyTwoWeekRangeHigh!!, fiftyTwoWeekRangeLow!!, volume!!, avgVolume!!, marketCap!!, 
        betaFiveYearMonthly!!, peRatioTTM!!, epsTTM!!, nextEarningsDate, forwardDivYieldValue!!,
        forwardDivYieldPercentage!!, exDividendDate, oneYearTargetEstimate!!,
        prepostPrice = prepostPrice!!,
        prepostChangeDollar = prepostChangeDollar!!,
        prepostChangePct = prepostChangePct!!,
        marketCapAbbreviatedString = marketCapAbbreviatedString!!
    )
}


@Entity(tableName = DbConstants.STOCK_QUOTE_TABLE)
data class StockQuoteEntity(
    @PrimaryKey val ticker: String = "",
    @ColumnInfo val timeSaved: Long = System.currentTimeMillis(),

    @ColumnInfo val changeDollarToday: Double? = null,
    @ColumnInfo val changePctToday: Double? = null,
    @ColumnInfo val curPrice: Double? = null,
    
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
    @ColumnInfo val prepostPrice: Double? = null,
    @ColumnInfo val prepostChangeDollar: Double? = null,
    @ColumnInfo val prepostChangePct: Double? = null,
    @ColumnInfo val marketCapAbbreviatedString: String? = null,

    )