package com.willor.ktstockdata

import com.willor.ktstockdata.historical_data.History
import com.willor.ktstockdata.historical_data.charts.simplechart.SimpleStockChart
import com.willor.ktstockdata.quote_data.Quotes


fun main() {

    val test = Quotes().getStockQuote("DNA")

    println(test)
}

