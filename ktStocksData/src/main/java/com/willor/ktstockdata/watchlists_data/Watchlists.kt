package com.willor.ktstockdata.watchlists_data

import com.willor.ktstockdata.common.getRandomUserAgent
import com.willor.ktstockdata.common.parseDouble
import com.willor.ktstockdata.common.parseLongFromBigAbbreviatedNumbers
import com.willor.ktstockdata.watchlists_data.dataobjects.Ticker
import com.willor.ktstockdata.watchlists_data.dataobjects.Watchlist
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Watchlists {

    // TODO Add a search for keyword function

    private fun scrapeYfWatchList(url: String): List<List<String>>? {
        try {
            val doc: Document? = Jsoup.connect(url)
                .userAgent(getRandomUserAgent())
                .get()

            val rows = doc?.select("tr") ?: return null

            val uglyDataCollection = mutableListOf<List<String>>()
            for (r in rows) {
                val dataPoints = r.select("td")
                if (!dataPoints.isNullOrEmpty()) {
                    val listOfDataPoints = mutableListOf<String>()
                    dataPoints.map {
                        listOfDataPoints.add(
                            it.text()
                        )
                    }
                    uglyDataCollection.add(listOfDataPoints)
                }
            }
            return uglyDataCollection
        } catch (e: Exception) {
            return null
        }
    }

    private fun processYfWatchlistScrape(url: String): Watchlist? {
        val createTicker = { rowData: List<String> ->
            try {

                var returnValue: Ticker? = null
                when (rowData.size) {
                    10 -> {
                        if (!rowData[0].contains("-")) {
                            returnValue = Ticker(
                                ticker = rowData[0].replace(" ", ""),
                                companyName = rowData[1],
                                lastPrice = parseDouble(rowData[2]),
                                changeDollar = parseDouble(rowData[3]),
                                changePercent = parseDouble(rowData[4].substringBefore("%")),
                                volume = parseLongFromBigAbbreviatedNumbers(rowData[5]).toInt(),
                                volumeThirtyDayAvg = parseLongFromBigAbbreviatedNumbers(rowData[6]).toInt(),
                                marketCap = parseLongFromBigAbbreviatedNumbers(rowData[7])
                            )
                        }
                    }

                    9 -> {
                        if (!rowData[0].contains("-")){
                            returnValue = Ticker(
                                ticker = rowData[0].replace(" ", ""),
                                companyName = rowData[1],
                                lastPrice = parseDouble(rowData[2]),
                                changeDollar = parseDouble(rowData[3]),
                                changePercent = parseDouble(rowData[4].substringBefore("%")),
                                volume = parseLongFromBigAbbreviatedNumbers(rowData[6]).toInt(),
                                volumeThirtyDayAvg = parseLongFromBigAbbreviatedNumbers(rowData[7]).toInt(),
                                marketCap = parseLongFromBigAbbreviatedNumbers(rowData[8])
                            )
                        }
                    }
                }
                returnValue
            } catch (e: Exception) {
                println(e.printStackTrace())
                null
            }
        }

        val getWatchlistName = { s: String ->

            if (s.contains("/watchlists/")) {
                s.substringAfterLast("/watchlists/")
            } else {
                s.substringAfter(".com/")
            }
        }

        val l = scrapeYfWatchList(url) ?: return null

        val wlName = getWatchlistName(url)

        val tickers = mutableListOf<Ticker>()
        for (n in 0..l.lastIndex) {
            val listOfValues = l[n]

            if (listOfValues.size == 9 || listOfValues.size == 10) {
                val t = (createTicker(l[n]))
                if (t != null) {
                    tickers.add(t)
                }
            }

        }

        return Watchlist(wlName, tickers)
    }

    fun searchForWatchlistByKeywords(vararg keywords: String): List<WatchlistNames>?{
        val matches = mutableListOf<WatchlistNames>()

        for(w in WatchlistNames.values()){
            for (k in keywords){
                if (w.name.contains(k.uppercase())){
                    matches.add(w)
                }
            }
        }

        // If matches were found, return them, else null
        return if (matches.size > 0){
            matches
        }else{
            null
        }
    }

    fun getWatchlist(w: WatchlistNames): Watchlist?{
        when (w) {
            WatchlistNames.GAINERS -> {
                return getGainers()
            }
            WatchlistNames.LOSERS -> {
                return getLosers()
            }
            WatchlistNames.TECH_STOCKS_THAT_MOVE_THE_MARKET -> {
                return getTechStocksThatMoveTheMarket()
            }
            WatchlistNames.MOST_ACTIVE -> {
                return getMostActive()
            }
            WatchlistNames.MOST_ACTIVE_PENNY_STOCKS -> {
                return getMostActivePennyStocks()
            }
            WatchlistNames.MOST_ACTIVE_SMALL_CAP_STOCKS-> {
                return getMostActiveSmallCapStocks()
            }
            WatchlistNames.MOST_BOUGHT_BY_ACTIVE_HEDGE_FUNDS -> {
                return getMostBoughtByActiveHedgeFunds()
            }
            WatchlistNames.MOST_BOUGHT_BY_HEDGE_FUNDS -> {
                return getMostBoughtByHedgeFunds()
            }
            WatchlistNames.MOST_SOLD_BY_ACTIVE_HEDGE_FUNDS -> {
                return getMostSoldByActiveHedgeFunds()
            }
            WatchlistNames.MOST_SOLD_BY_HEDGE_FUNDS-> {
                return getMostSoldByHedgeFunds()
            }
            WatchlistNames.MOST_NEWLY_ADDED_TO_WATCHLISTS-> {
                return getMostNewlyAddedToWatchlists()
            }
            WatchlistNames.MOST_WATCHED_BY_RETAIL_TRADERS-> {
                return getMostWatchedByRetailTraders()
            }
            WatchlistNames.LARGEST_FIFTY_TWO_WEEK_GAINS-> {
                return getLargestFiftyTwoWeekGains()
            }
            WatchlistNames.LARGEST_FIFTY_TWO_WEEK_LOSSES-> {
                return getLargestFiftyTwoWeekLosses()
            }
            WatchlistNames.RECENT_FIFTY_TWO_WEEK_HIGHS-> {
                return getRecentFiftyTwoWeekHighs()
            }
            WatchlistNames.RECENT_FIFTY_TWO_WEEK_LOWS-> {
                return getRecentFiftyTwoWeekLows()
            }
            WatchlistNames.BIG_EARNINGS_MISSES-> {
                return getBigEarningsMisses()
            }
            WatchlistNames.BIG_EARNINGS_BEATS-> {
                return getBigEarningsBeats()
            }
            WatchlistNames.CHINA_TECH_AND_INTERNET_STOCKS-> {
                return getChinaTechAndInternet()
            }
            WatchlistNames.E_COMMERCE_STOCKS-> {
                return getECommerceStocks()
            }
            WatchlistNames.CANNABIS_STOCKS-> {
                return getCannabisStocks()
            }
            WatchlistNames.SELF_DRIVING_CAR_STOCKS-> {
                return getSelfDrivingCarStocks()
            }
            WatchlistNames.VIDEO_GAME_DEVELOPER_STOCKS-> {
                return getVideoGameStocks()
            }
            WatchlistNames.BANKS_AND_FINANCIAL_SERVICES_STOCKS-> {
                return getBankAndFinancialServicesStocks()
            }
            WatchlistNames.MEDICAL_DEVICE_AND_RESEARCH_STOCKS-> {
                return getSelfDrivingCarStocks()
            }
            WatchlistNames.SMART_MONEY_STOCKS-> {
                return getSmartMoneyStocks()
            }
            WatchlistNames.DIVIDEND_GROWTH_MARKET_LEADERS-> {
                return getDividendGrowthMarketLeaders()
            }
            WatchlistNames.BERKSHIRE_HATHAWAY_PORTFOLIO-> {
                return getBerkshireHathawayPortfolio()
            }
            WatchlistNames.BIOTECH_AND_DRUG_STOCKS-> {
                return getBioTechAndDrugStocks()
            }
        }
    }

    fun getGainers(): Watchlist? {
        return processYfWatchlistScrape("https://finance.yahoo.com/gainers") ?: return null
    }

    fun getLosers(): Watchlist? {
        return processYfWatchlistScrape("https://finance.yahoo.com/losers")
    }

    fun getTechStocksThatMoveTheMarket(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/" +
                    "watchlists/tech-stocks-that-move-the-market"
        )

    }

    fun getMostActive(): Watchlist? {
        return processYfWatchlistScrape("https://finance.yahoo.com/most-active")
    }

    fun getMostActivePennyStocks(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/watchl" +
                    "ists/most-active-penny-stocks"
        )
    }

    fun getMostActiveSmallCapStocks(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo" +
                    "-finance/watchlists/most-active-small-cap-stocks"
        )
    }

    fun getMostBoughtByActiveHedgeFunds(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance" +
                    "/watchlists/most-bought-by-activist-hedge-funds"
        )
    }

    fun getMostBoughtByHedgeFunds(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo" +
                    "-finance/watchlists/most-bought-by-hedge-funds"
        )
    }

    fun getMostSoldByActiveHedgeFunds(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/watch" +
                    "lists/most-sold-by-activist-hedge-funds"
        )
    }

    fun getMostSoldByHedgeFunds(): Watchlist?{
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/watchlists/m" +
                    "ost-sold-by-hedge-funds"
        )
    }

    fun getMostNewlyAddedToWatchlists(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/w" +
                    "atchlists/most-added"
        )
    }

    fun getMostWatchedByRetailTraders(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yaho" +
                    "o-finance/watchlists/most-watched"
        )
    }

    fun getLargestFiftyTwoWeekGains(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-fi" +
                    "nance/watchlists/fiftytwo-wk-gain"
        )
    }

    fun getLargestFiftyTwoWeekLosses(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/watchl" +
                    "ists/fiftytwo-wk-loss"
        )
    }

    fun getRecentFiftyTwoWeekHighs(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/watchl" +
                    "ists/fiftytwo-wk-high"
        )
    }

    fun getRecentFiftyTwoWeekLows(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo" +
                    "-finance/watchlists/fiftytwo-wk-low"
        )
    }

    fun getBigEarningsMisses(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/w" +
                    "atchlists/earnings-miss"
        )

    }

    fun getBigEarningsBeats(): Watchlist?{
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/" +
                    "watchlists/earnings-beat"
        )

    }

    fun getChinaTechAndInternet(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/watchlist" +
                    "s/china-tech-and-internet-stocks"
        )
    }

    fun getECommerceStocks(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/" +
                    "watchlists/e-commerce-stocks"
        )
    }

    fun getCannabisStocks(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/watchlist" +
                    "s/420_stocks"
        )
    }

    fun getSelfDrivingCarStocks(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance" +
                    "/watchlists/the-autonomous-car"
        )
    }

    fun getVideoGameStocks(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-f" +
                    "inance/watchlists/video-game-stocks"
        )
    }

    fun getBankAndFinancialServicesStocks(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-fi" +
                    "nance/watchlists/bank-and-financial-services-stocks"
        )
    }

    fun getMedicalDeviceAndResearchStocks(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo." +
                    "com/u/yahoo-finance/watchlists/medical-device-and-research-stocks"
        )
    }

    fun getSmartMoneyStocks(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/watchl" +
                    "ists/smart-money-stocks"
        )
    }

    fun getDividendGrowthMarketLeaders(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/motley-fool/watchlists/div" +
                    "idend-growth-market-leaders"
        )
    }

    fun getBerkshireHathawayPortfolio(): Watchlist? {
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/watchl" +
                    "ists/the-berkshire-hathaway-portfolio"
        )
    }

    fun getBioTechAndDrugStocks(): Watchlist?{
        return processYfWatchlistScrape(
            "https://finance.yahoo.com/u/yahoo-finance/watchlists/biotech-and-drug-stocks"
        )
    }

}

fun main() {
    println(Watchlists().getWatchlist(WatchlistNames.BIOTECH_AND_DRUG_STOCKS))
}

