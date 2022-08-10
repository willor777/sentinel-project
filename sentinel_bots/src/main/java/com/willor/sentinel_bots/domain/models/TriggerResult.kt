package com.willor.sentinel_bots.domain.models

data class TriggerResult(
    val ticker: String,
    val trigger: Int,        // 1, 0, -1 for Long, Neutral, Short
    val rating: Int         // Value 1-10
)