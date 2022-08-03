package com.willor.ktstockdata.historical_data.dataobjects.candle

import java.util.*

data class Candle(
    val datetime: Date,
    val timestamp: Int,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Int
)