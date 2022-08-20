package com.willor.sentinel_bots.domain.models

import com.willor.ktstockdata.historical_data.charts.advancedchart.AdvancedStockChart

data class TriggerBase(
    val timestamp: Long,
    val ticker: String,
    val triggerValue: Int,
    val rating: Int,
    val strategyName: String,
    val strategyDescription: String,
    val priceOfAsset: Double,
    val volumeOfTriggerCandle: Int
)