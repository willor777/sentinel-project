package com.willor.sentinelscanners.domain.models

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