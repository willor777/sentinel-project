package com.willor.ktstockdata.misc_data.dataobjects

data class MajorFuturesData(
    val sp500Future: Future?,
    val dowFuture: Future?,
    val nasdaqFuture: Future?,
    val russel2000Future: Future?,
    val usTreasuryBondFuture: Future?,
    val usTenYearTreasuryNoteFuture: Future?,
    val usFiveYearTreasuryNoteFuture: Future?,
    val usTwoYearTreasureNoteFuture: Future?,
    val goldFuture: Future?,
    val goldMicroFuture: Future?,
    val silverFuture: Future?,
    val microSilverFuture: Future?,
    val crudeOilWTIFuture: Future?,
    val rbobGasolineFuture: Future?,
    val brentCrudeOilFuture: Future?,
)
