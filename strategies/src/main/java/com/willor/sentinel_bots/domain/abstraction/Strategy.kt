package com.willor.sentinel_bots.domain.abstraction

import com.willor.ktstockdata.historical_data.charts.advancedchart.AdvancedStockChart
import com.willor.sentinel_bots.domain.models.StrategyResultSet


interface Strategy {

    val strategyName: String
    val strategyDescription: String

    fun runAnalysisOnCharts(charts: List<AdvancedStockChart>): StrategyResultSet


}