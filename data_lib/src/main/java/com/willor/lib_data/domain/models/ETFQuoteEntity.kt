package com.willor.lib_data.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.willor.ktstockdata.quote_data.dataobjects.ETFQuote
import com.willor.lib_data.utils.DbConstants
import java.util.*

fun ETFQuoteEntity.toETFQuote(): ETFQuote{
    return ETFQuote(
        this.ticker!!, this.changeDollarToday!!, this.changePctToday!!, this.curPrice!!,
        this.prevClose!!, this.openPrice!!, this.bidPrice!!, this.bidSize!!, this.askPrice!!,
        this.askSize!!, this.daysRangeHigh!!, this.daysRangeLow!!, this.fiftyTwoWeekRangeHigh!!,
        this.fiftyTwoWeekRangeLow!!, this.volume!!, this.avgVolume!!, this.netAssets!!, this.nav!!,
        this.peRatioTTM!!, this.yieldPercentage!!, this.yearToDateTotalReturn!!, this.betaFiveYearMonthly!!,
        this.expenseRatioNetPercentage!!, this.inceptionDate
    )
}

fun ETFQuote.toETFQuoteEntity(): ETFQuoteEntity{
    return ETFQuoteEntity(
        ticker = ticker, changeDollarToday = changeDollarToday,
        changePctToday = changePctToday, curPrice = curPrice, prevClose = prevClose,
        openPrice = openPrice, bidPrice = bidPrice,
        bidSize = bidSize, askPrice = askPrice, askSize = askSize, daysRangeHigh = daysRangeHigh,
        daysRangeLow = daysRangeLow, fiftyTwoWeekRangeHigh = fiftyTwoWeekRangeHigh,
        fiftyTwoWeekRangeLow = fiftyTwoWeekRangeLow, volume = volume, avgVolume = avgVolume,
        netAssets = netAssets, nav = nav, peRatioTTM = peRatioTTM,
        yieldPercentage = yieldPercentage, yearToDateTotalReturn = yearToDateTotalReturn,
        betaFiveYearMonthly = betaFiveYearMonthly,
        expenseRatioNetPercentage = expenseRatioNetPercentage,
        inceptionDate = inceptionDate
    )
}

@Entity(tableName = DbConstants.ETF_QUOTE_TABLE)
data class ETFQuoteEntity(
    @PrimaryKey val ticker: String = "NONE",
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
    @ColumnInfo val netAssets: Long? = null,
    @ColumnInfo val nav: Double? = null,
    @ColumnInfo val peRatioTTM: Double? = null,
    @ColumnInfo val yieldPercentage: Double? = null,
    @ColumnInfo val yearToDateTotalReturn: Double? = null,
    @ColumnInfo val betaFiveYearMonthly: Double? = null,
    @ColumnInfo val expenseRatioNetPercentage: Double? = null,
    @ColumnInfo val inceptionDate: Date? = null,
)