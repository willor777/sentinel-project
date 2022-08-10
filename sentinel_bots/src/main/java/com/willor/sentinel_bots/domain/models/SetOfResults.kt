package com.willor.sentinel_bots.domain.models

data class SetOfResults(
    val botName: String,
    val botDescription: String,
    val triggers: List<TriggerResult>
)