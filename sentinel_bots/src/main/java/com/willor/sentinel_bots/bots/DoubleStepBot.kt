package com.willor.sentinel_bots.bots

import com.willor.ktstockdata.historical_data.History
import com.willor.ktstockdata.historical_data.charts.advancedchart.AdvancedStockChart
import com.willor.lib_data.data.remote.StockData
import com.willor.lib_data.domain.abstraction.IRepo
import com.willor.lib_data.domain.abstraction.Resource
import com.willor.lib_data.domain.models.AdvChartEntity
import com.willor.lib_data.utils.handleErrorsToLog
import com.willor.lib_data.utils.printToDEBUGTEMP
import com.willor.sentinel_bots.domain.abstraction.SentinelBot
import com.willor.sentinel_bots.domain.models.SetOfResults
import com.willor.sentinel_bots.domain.models.TriggerResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject


class DoubleStepBot @Inject constructor(
    private val api: IRepo
): SentinelBot{

    override val botName: String = "DoubleStep"
    override val botDescription: String = ""


    override suspend fun runScan(tickers: List<String>): SetOfResults? {

        val results = mutableListOf<TriggerResult>()
        for (t in tickers){
            results.add(scanTicker(t))
        }

        return null
    }

    private suspend fun collectChart(t: String): AdvancedStockChart? {

        var result: AdvancedStockChart? = null

        // Make the call and wait for result
        api.getHistoricDataAsAdvancedStockChart(
            t, periodRange = "2d"
        ).handleErrorsToLog().collect() {
            when(it){

                is Resource.Loading -> {
                    printToDEBUGTEMP("$botName received loading for $t chart")
                }

                is Resource.Success -> {
                    printToDEBUGTEMP("$botName collected $t chart")
                    result = it.data
                }

                is Resource.Error -> {
                    printToDEBUGTEMP("$botName failed to collect $t chart")
                }
            }
        }

        return result
    }


    private suspend fun scanTicker(t: String): TriggerResult{
        val chart: AdvancedStockChart = collectChart(t) ?: return TriggerResult(t, 0, 0)
        printToDEBUGTEMP("Collected chart")

        val t1 = chart.lastIndex - 1
        val t2 = chart.lastIndex - 2

        var trigger = 0

        with(chart){

            // Bullish
            if (closedAtHigh(t2)){
                if (chart.getCloseAtIndex(t1) > chart.getCloseAtIndex(t2)){
                    trigger = 1
                }
            }

            // Bearish
            if (closedAtLow(t2)){
                if (chart.getCloseAtIndex(t1) < chart.getCloseAtIndex(t2)){
                    trigger = -1
                }
            }
        }

        return TriggerResult(t, trigger, 10)
    }
}