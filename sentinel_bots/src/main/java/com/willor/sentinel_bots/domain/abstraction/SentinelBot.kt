package com.willor.sentinel_bots.domain.abstraction

import com.willor.sentinel_bots.domain.models.SetOfResults

interface SentinelBot {

    val botName: String
    val botDescription: String

    suspend fun runScan(tickers: List<String>): SetOfResults?
}