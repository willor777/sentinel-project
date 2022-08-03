package com.willor.ktstockdata.misc_data.dataobjects

data class Index(
    val ticker: String,
    val name: String,
    val lastPrice: Double,
    val changeDollar: Double,
    val changePercent: Double,
    val volume: Long
)
