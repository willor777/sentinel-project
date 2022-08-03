package com.willor.ktstockdata.quote_data.dataobjects

import java.util.*

data class ETFQuote(
    val ticker: String,
    val prevClose: Double,
    val openPrice: Double,
    val bidPrice: Double,
    val bidSize: Int,
    val askPrice: Double,
    val askSize: Int,
    val daysRangeHigh: Double,
    val daysRangeLow: Double,
    val fiftyTwoWeekRangeHigh: Double,
    val fiftyTwoWeekRangeLow: Double,
    val volume: Int,
    val avgVolume: Int,
    val netAssets: Long,
    val nav: Double,
    val peRatioTTM: Double,
    val yieldPercentage: Double,
    val yearToDateTotalReturn: Double,
    val betaFiveYearMonthly: Double,
    val expenseRatioNetPercentage: Double,
    val inceptionDate: Date,
    )

