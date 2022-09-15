package com.willor.sentinelscanners.domain.abstraction

import com.willor.ktstockdata.historicchartdata.charts.advancedchart.AdvancedStockChart
import com.willor.sentinelscanners.domain.models.StrategyResultSet


interface Strategy {

    val strategyName: String
    val strategyDescription: String

    fun runAnalysisOnCharts(charts: List<AdvancedStockChart>): StrategyResultSet


}