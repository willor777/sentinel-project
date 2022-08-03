- KtStockData

Hello
- History Usage...


```kotlin


// Create History Object
val history = History()

// If you just want the stock data
val simpleChart: SimpleStockChart = history.getHistoryAsSimpleStockChart(
    "SPY", interval = "5m", period = "7d", prepost = true
)!!

// Access values using properties
val v1: Double = simpleChart.close[simpleChart.close.lastIndex]

// Access values using getters, Accepts negative index (-1 == lastIndex)
val v2: Double = simpleChart.getCloseAtIndex(-1)

// Access values as candle, Accepts negative index
val v3: Candle = simpleChart.getCandleAtIndex(-1)


// If you intend to perform analysis on the chart use Advanced Stock Chart
val advancedChart: AdvancedStockChart = history.getHistoryAsAdvancedStockChart(
    "SPY", interval = "5m", period = "7d", prepost = true
)!!

// Access values the same way as SimpleStockChart
val v4 = advancedChart.getCandleAtIndex(-1)

// Technical Analysis methods are in progress and may not work properly

// Basic TA methods available here. (More will be added)
val lastCandleRange= advancedChart.getCandleHighToLowMeasurement(-1)

// Get indicators like so...
val sma: SMA = SMA(advancedChart.close, 20)

val boll: BollingerBands = BollingerBands(advancedChart.close)

val atr: AverageTrueRange = AverageTrueRange(advancedChart.getSublistOfCandles(-60, -1))


```