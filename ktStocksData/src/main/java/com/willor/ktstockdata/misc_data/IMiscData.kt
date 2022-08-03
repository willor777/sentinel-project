package com.willor.ktstockdata.misc_data

import com.willor.ktstockdata.misc_data.dataobjects.MajorFuturesData
import com.willor.ktstockdata.misc_data.dataobjects.MajorIndicesData
import com.willor.ktstockdata.misc_data.dataobjects.SnRLevels

interface IMiscData {

    fun getSupportAndResistanceFromBarchartDotCom(ticker: String): SnRLevels?


    fun getFuturesData(): MajorFuturesData?


    fun getMajorIndicesData(): MajorIndicesData?
}

