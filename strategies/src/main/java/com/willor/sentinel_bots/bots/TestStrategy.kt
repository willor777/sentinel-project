package com.willor.sentinel_bots.bots

import com.willor.ktstockdata.historical_data.charts.advancedchart.AdvancedStockChart
import com.willor.sentinel_bots.domain.abstraction.Strategy
import com.willor.sentinel_bots.domain.models.StrategyResultSet
import com.willor.sentinel_bots.domain.models.TriggerBase

class TestStrategy: Strategy {

    override val strategyName: String
        get() = "TestBot"
    override val strategyDescription: String
        get() = "Bot made for testing purposes"

    var lastTrigger = 1     // TODO Delete ME !!!

    override fun runAnalysisOnCharts(charts: List<AdvancedStockChart>): StrategyResultSet {

        val triggers = mutableListOf<TriggerBase>()
        charts.map{
            triggers.add(
                scanChart(it)
            )
        }

        return StrategyResultSet(
            strategyName, strategyDescription, triggers
        )

    }

    private fun scanChart(c: AdvancedStockChart): TriggerBase{
        var trigger = 0

        // Long
        if (c.isGreenCandle(-3) && c.getCloseAtIndex(-2) > c.getCloseAtIndex(-3)){
            trigger = 1
        }

        else if (
            c.isRedCandle(-3) && c.getCloseAtIndex(-2) < c.getCloseAtIndex(-3)
        ){
            trigger = -1
        }

        // TODO Delete Me!! Testing Purposes Only
//        if (lastTrigger == 1){
//            lastTrigger = -1
//            trigger = -1
//        }else{
//            lastTrigger = 1
//            trigger = 1
//        }

        return TriggerBase(timestamp = System.currentTimeMillis(), ticker = c.ticker,
        triggerValue = trigger, rating = 10, strategyName = strategyName,
            strategyDescription = strategyDescription, priceOfAsset = c.getCloseAtIndex(-1),
        volumeOfTriggerCandle = c.getVolumeAtIndex(-2))
    }
}