package com.willor.ktstockdata.misc_data.dataobjects

data class MajorIndicesData(
    val sp500 : Index?,
    val dow : Index?,
    val nasdaq: Index?,
    val russel2000: Index?,
    val vix: Index?,
    val ftse100: Index?,
    val hengSeng: Index?
)
