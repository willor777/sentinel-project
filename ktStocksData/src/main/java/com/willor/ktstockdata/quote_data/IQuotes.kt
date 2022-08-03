package com.willor.ktstockdata.quote_data

import com.willor.ktstockdata.quote_data.dataobjects.ETFQuote
import com.willor.ktstockdata.quote_data.dataobjects.OptionStats
import com.willor.ktstockdata.quote_data.dataobjects.StockQuote

interface IQuotes {

    fun getStockQuote(ticker: String): StockQuote?

    fun getETFQuote(ticker: String): ETFQuote?

    fun getOptionStats(ticker: String): OptionStats?

}


