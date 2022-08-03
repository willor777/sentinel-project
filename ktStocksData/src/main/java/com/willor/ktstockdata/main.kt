package com.willor.ktstockdata

import com.willor.ktstockdata.historical_data.History
import com.willor.ktstockdata.historical_data.charts.simplechart.SimpleStockChart


fun main() {
// Create History Object
    val history = History()

// If you just want the stock data
    val simpleChart: SimpleStockChart = history.getHistoryAsSimpleStockChart(
        "SPY", interval = "5m", periodRange = "7d", prepost = true
    )!!

    println(simpleChart.close)
}

