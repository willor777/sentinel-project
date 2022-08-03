package com.willor.ktstockdata.misc_data

import com.willor.ktstockdata.common.*
import com.willor.ktstockdata.common.getRandomUserAgent
import com.willor.ktstockdata.common.parseDouble
import com.willor.ktstockdata.misc_data.dataobjects.*
import org.jsoup.Jsoup
import kotlin.Exception

class MiscData: IMiscData{

    /**
     * Scrapes the finance.yahoo.com World Index List. Only returns the Major ones as a
     * MajorIndicesData? Object filled with Index objects for...
     *
     * SP500
     * Dow
     * Nasdaq
     * Russel 2000
     * VIX
     * FTSE (Europe)
     * HengSeng (China)
     */
    override fun getMajorIndicesData(): MajorIndicesData? {

        val stripDataGetIndex = {data: List<String> ->
            try{
                Index(
                    data[0].replace(" ", ""),
                    data[1],
                    parseDouble(data[2]),
                    parseDouble(data[3]),
                    parseDouble(data[4].substringBefore("%")),
                    parseLongFromBigAbbreviatedNumbers(data[5])
                )

            }catch (e: Exception){
                Log.d("EXCEPTION", e.stackTraceToString() + "\n CAUSE...$data")
                null
            }
        }

        try{
            val url = "https://finance.yahoo.com/world-indices"
            val document = Jsoup.connect(url).userAgent(getRandomUserAgent()).get() ?: return null

            val table = document.select("section#yfin-list")
            val rows = table.select("tr")

            // Data starts at index 1
            // Symbol, Name, LastPrice, MarketTime, ChangeDollar, ChangePercent, Volume(IntAbbre), AvgVolu(IntAbbre)
            val dataList = mutableListOf<List<String>>()
            for (n in 1..rows.lastIndex) {
                val cleanInnerData = mutableListOf<String>()
                val innerdata = rows[n].select("td")
                for (i in 0..innerdata.lastIndex) {
                    cleanInnerData.add(innerdata[i].text().replace(" ", ""))
                }
                dataList.add(cleanInnerData)
            }

            val indexMap = mutableMapOf<String, Index?>()
            indexMap["sp"] = null; indexMap["dow"] = null; indexMap["nas"] = null
            indexMap["russ"] = null; indexMap["vix"] = null; indexMap["ftse"] = null
            indexMap["heng"] = null
            for (n in 0..dataList.lastIndex){
                val data = dataList[n]

                when(data[0]){
                    "^GSPC" ->{
                        indexMap["sp"] = stripDataGetIndex(data)
                    }
                    "^DJI" ->{
                        indexMap["dow"] = stripDataGetIndex(data)
                    }
                    "^IXIC" ->{
                        indexMap["nas"] = stripDataGetIndex(data)
                    }
                    "RUT" ->{
                        indexMap["russ"] = stripDataGetIndex(data)
                    }
                    "^VIX" ->{
                        indexMap["vix"] = stripDataGetIndex(data)
                    }
                    "^FTSE" ->{
                        indexMap["ftse"] = stripDataGetIndex(data)
                    }
                    "^HSI" ->{
                        indexMap["heng"] = stripDataGetIndex(data)
                    }
                }

            }

            return MajorIndicesData(
                sp500 = indexMap["sp"],
                dow = indexMap["dow"],
                nasdaq = indexMap["nas"],
                russel2000 = indexMap["russ"],
                vix = indexMap["vix"],
                ftse100 = indexMap["ftse"],
                hengSeng = indexMap["heng"]
            )

        }catch (e: Exception){
            Log.d("EXCEPTION", e.stackTraceToString())
            return null
        }
    }


    /**
     * Scrapes the finance.yahoo.com Futures list.
     * - Retrieves data for...
     *      - All major Index Futures
     *      - US Treasury Bond, 10yr Note, 5yr Note, 2yr Note
     *      - Gold, Silver
     *      - Oil, Gas
     * - Returns
     *      - [FuturesData]? : Which is a data class containing [Future] objects
     */
    override fun getFuturesData(): MajorFuturesData?{
        try{
            val url = "https://finance.yahoo.com/commodities"
            val document = Jsoup.connect(url).userAgent(getRandomUserAgent()).get() ?: return null

            val table = document.select("section#yfin-list")
            val rows = table.select("tr")

            // Data starts at index 1
            // Symbol, Name, LastPrice, MarketTime, ChangeDollar, ChangePercent, Volume(IntAbbre), AvgVolu(IntAbbre)
            val dataList = mutableListOf<List<String>>()
            for (n in 1..rows.lastIndex) {
                val cleanInnerData = mutableListOf<String>()
                val innerdata = rows[n].select("td")
                for (i in 0..innerdata.lastIndex) {
                    cleanInnerData.add(innerdata[i].text().replace(" ", ""))
                }
                dataList.add(cleanInnerData)
            }

            return MajorFuturesData(
                sp500Future = Future.createFromList(dataList[0]),
                dowFuture = Future.createFromList(dataList[1]),
                nasdaqFuture = Future.createFromList(dataList[2]),
                russel2000Future = Future.createFromList(dataList[3]),
                usTreasuryBondFuture = Future.createFromList(dataList[4]),
                usTenYearTreasuryNoteFuture = Future.createFromList(dataList[5]),
                usFiveYearTreasuryNoteFuture = Future.createFromList(dataList[6]),
                usTwoYearTreasureNoteFuture = Future.createFromList(dataList[7]),
                goldFuture = Future.createFromList(dataList[8]),
                goldMicroFuture = Future.createFromList(dataList[9]),
                silverFuture = Future.createFromList(dataList[10]),
                microSilverFuture = Future.createFromList(dataList[11]),
                crudeOilWTIFuture = Future.createFromList(dataList[15]),
                rbobGasolineFuture = Future.createFromList(dataList[18]),
                brentCrudeOilFuture = Future.createFromList(dataList[19])
            )
        }catch(e: Exception){
            Log.d("EXCEPTION", e.stackTraceToString())
            return null
        }
    }

    /**
     * Scrapes the Support and Resistance levels from Barchart.com Quote Page.
     *
     * - Includes...
     *
     *      - Approximate Last Price
     *
     *      - S1, S2, S3, R1, R2, R3
     *
     *      - 52wk High, Fib 61.8%, Fib 50%, Fib 38.2%, 52wk Low
     */
    override fun getSupportAndResistanceFromBarchartDotCom(ticker: String): SnRLevels?{
        try{
            // Get Document
            val tick = ticker.uppercase()
            val url = "https://www.barchart.com/stocks/quotes/${tick}/overview"
            val doc = Jsoup.connect(url).userAgent(getRandomUserAgent()).get() ?: return null

            // Pull data rows
            val table = doc.select("div.bc-side-widget")
            val rows = table.select("td")

            // Build list of all data
            val allData = mutableListOf<String>()
            for (r in rows) {
                allData.add(r.text())
            }

            // Snr data starts here -- Note there is other valuable data on this page for future ref...
            val indexStart = allData.indexOf("3rd Resistance Point")

            val cleanValues = mutableListOf<String>()
            for (n in indexStart..allData.lastIndex) {
                cleanValues.add(allData[n].replace(" ", ""))
            }

            return SnRLevels(
                ticker = tick,
                r3 = parseDouble(cleanValues[1]),
                r2 = parseDouble(cleanValues[3]),
                r1 = parseDouble(cleanValues[5]),
                approxPrice = parseDouble(cleanValues[7]),
                s1 = parseDouble(cleanValues[9]),
                s2 = parseDouble(cleanValues[11]),
                s3 = parseDouble(cleanValues[13]),
                fiftyTwoWeekHigh = parseDouble(cleanValues[15]),
                fibonacci62Pct = parseDouble(cleanValues[17]),
                fibonacci50Pct = parseDouble(cleanValues[19]),
                fibonacci38Pct = parseDouble(cleanValues[21]),
                fiftyTwoWeekLow = parseDouble(cleanValues[25])
            )
        } catch(e: Exception){
            Log.d("EXCEPTION", e.stackTraceToString())
            return null
        }
    }
}


