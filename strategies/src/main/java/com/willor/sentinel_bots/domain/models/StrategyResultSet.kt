package com.willor.sentinel_bots.domain.models

data class StrategyResultSet(
    val strategyName: String,
    val strategyDescription: String,
    val triggers: List<TriggerBase>
)