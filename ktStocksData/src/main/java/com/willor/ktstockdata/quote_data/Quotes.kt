package com.willor.ktstockdata.quote_data

import android.annotation.SuppressLint
import com.willor.ktstockdata.common.*
import com.willor.ktstockdata.common.getRandomUserAgent
import com.willor.ktstockdata.common.parseDouble
import com.willor.ktstockdata.common.parseInt
import com.willor.ktstockdata.common.parseLongFromBigAbbreviatedNumbers

import com.willor.ktstockdata.quote_data.dataobjects.ETFQuote
import com.willor.ktstockdata.quote_data.dataobjects.OptionStats
import com.willor.ktstockdata.quote_data.dataobjects.StockQuote
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.lang.Exception
import java.text.SimpleDateFormat


/**
 * Includes methods to scrape Stock, Etf Quotes and Option Statistics.
 *
 * In the future i will add Crypto Quote as well
 */
@SuppressLint("SimpleDateFormat")           // Warning about "Should use..."
class Quotes: IQuotes {


    /**
     * Scrapes the main data table of a finance.yahoo.com Quote Screen.
     */
    private fun scrapeYfinanceQuoteTable(ticker: String): List<List<String>>?{
        try{
            val url = "https://finance.yahoo.com/quote/$ticker?p=$ticker&.tsrc=fin-srch"

            val page = NetworkClient.getWebpage(url) ?: return null

            val document: Document = Jsoup.parse(page)

            val table: Elements = document.select("div#quote-summary") ?: return null

            val dataRows: Elements = table.select("tr")

            val uglyData = mutableListOf<List<String>>()
            for (row in dataRows){
                val cols = row.children()

                val data = mutableListOf<String>()
                for (child in cols){
                    data.add(child.text())
                }
                uglyData.add(data)
            }

            return uglyData
        }catch (e: Exception){
            Log.d("EXCEPTION", e.stackTraceToString())
            return null
        }
    }


    /**
     * Scrapes the Overall Option Stats from Barchart.com... Includes Quote data as well.
     */
    private fun scrapeBarchartQuoteTable(ticker: String): List<List<String>>?{

        try{
            // NOTE -- Even though this url is for a Stock, it seems to work for ETFs as well
            // further testing is required
            val url = "https://www.barchart.com/stocks/quotes/$ticker/overview"
            val page = NetworkClient.getWebpage(url) ?: return null
            val doc: Document = Jsoup.parse(page)

            val tableOfOptionStats = doc.select(
                "div.block-content"
            )

            val dataRows = tableOfOptionStats.select("li")

            val uglyData = mutableListOf<List<String>>()
            for (row in dataRows) {
                val labelAndValues = row.select("span")

                val dataChunk = mutableListOf<String>()
                if (labelAndValues.size != 13 && labelAndValues.size != 0) {
                    labelAndValues.map {
                        if (it.text().isNotEmpty()) {
                            dataChunk.add(it.text())
                        }
                    }
                    uglyData.add(dataChunk.toList())
                }
            }
            return uglyData
        }catch(e: Exception){
            Log.d("EXCEPTION", e.stackTraceToString())
            return null
        }
    }


    /**
     * Retrieves ETF Quote from finance.yahoo.com. Will NOT work for stock quotes.
     */
    override fun getETFQuote(ticker: String): ETFQuote? {

        try{
            val dateFromString = { s: String ->
                SimpleDateFormat("yyyy-MM-dd").parse(s)
            }

            val tick = ticker.uppercase()

            val scrape = scrapeYfinanceQuoteTable(tick)!!

            // Make sure it's a stock by check for the Beta keyword at index 9 0
            if (!scrape[9][0].contains("NAV")) {
                return null
            }


            return ETFQuote(
                tick,
                parseDouble(scrape[0][1]),
                parseDouble(scrape[1][1]),
                parseDouble(scrape[2][1].substringBefore(" x ")),
                parseInt(scrape[2][1].substringAfter(" x ")),
                parseDouble(scrape[3][1].substringBefore(" x ")),
                parseInt(scrape[3][1].substringAfter(" x ")),
                parseDouble(scrape[4][1].substringAfter(" - ")),
                parseDouble(scrape[4][1].substringBefore(" - ")),
                parseDouble(scrape[5][1].substringAfter(" - ")),
                parseDouble(scrape[5][1].substringBefore(" - ")),
                parseInt(scrape[6][1]),
                parseInt(scrape[7][1]),
                parseLongFromBigAbbreviatedNumbers(scrape[8][1]),
                parseDouble(scrape[9][1]),
                parseDouble(scrape[10][1]),
                parseDouble(scrape[11][1].substringBefore("%")),
                parseDouble(scrape[12][1].substringBefore("%")),
                parseDouble(scrape[13][1]),
                parseDouble(scrape[14][1].substringBefore("%")),
                dateFromString(scrape[15][1])!!
            )
        } catch (e: Exception){
            Log.d("EXCEPTION", e.stackTraceToString())
            return null
        }
    }


    /**
     * Retrieves Stock Quote from finance.yahoo.com. Will NOT work for ETF quote
     */
    override fun getStockQuote(ticker: String): StockQuote? {
        try{
            val dateFromString = { s: String ->
                var str = s
                if (s.contains("-")){
                    str = s.substringBefore("-")
                }
                SimpleDateFormat("MMM dd, yyyy").parse(str)
            }

            val tick = ticker.uppercase()

            val scrape = scrapeYfinanceQuoteTable(tick)!!

            // Make sure it's a stock by check for the Beta keyword at index 9 0
            if (!scrape[9][0].contains("Beta")) {
                return null
            }

            // process values
            return StockQuote(
                tick,
                parseDouble(scrape[0][1]),
                parseDouble(scrape[1][1]),
                parseDouble(scrape[2][1].substringBefore("x")),
                parseInt(scrape[2][1].substringAfter("x")),
                parseDouble(scrape[3][1].substringBefore("x")),
                parseInt(scrape[3][1].substringAfter("x")),
                parseDouble(scrape[4][1].substringAfter("-")),
                parseDouble(scrape[4][1].substringBefore("-")),
                parseDouble(scrape[5][1].substringAfter("-")),
                parseDouble(scrape[5][1].substringBefore("-")),
                parseInt(scrape[6][1]),
                parseInt(scrape[7][1]),
                parseLongFromBigAbbreviatedNumbers(scrape[8][1]),
                parseDouble(scrape[9][1]),
                parseDouble(scrape[10][1]),
                parseDouble(scrape[11][1]),
                dateFromString(scrape[12][1])!!,
                parseDouble(scrape[13][1].substringBefore("(")),
                parseDouble(
                    scrape[13][1].substringAfter("(").substringBefore("%")
                ),
                dateFromString(scrape[14][1])!!,
                parseDouble(scrape[15][1])
            )
        } catch(e: Exception){
            Log.d("EXCEPTION", e.stackTraceToString())
            return null
        }
    }


    /**
     * Retrieves Option Statistics from www.barchart.com
     */
    override fun getOptionStats(ticker: String): OptionStats? {

        try{

            @SuppressLint("SimpleDateFormat")
            val stringDateToJavaData = { s: String ->
                SimpleDateFormat("MM/dd/yy").parse(s)
            }

            val tick = ticker.uppercase()

            val scrapedStats = scrapeBarchartQuoteTable(tick) ?: return null

            // Find starting index... It's different for Stock / ETF...Build list of target data
            var foundStart = false
            val dataActual = mutableListOf<List<String>>()
            for (n in 0..scrapedStats.lastIndex) {
                if (foundStart) {
                    dataActual.add(scrapedStats[n])
                    continue
                }
                if (scrapedStats[n].contains("Implied Volatility")) {
                    foundStart = true
                    dataActual.add(scrapedStats[n])
                }
            }

            return OptionStats(
                tick,
                parseDouble(dataActual[0][1].substringBefore("%")),
                parseDouble(dataActual[0][1] .substringAfter(
                    "(").substringBefore("%")),
                parseDouble(
                    dataActual[1][1].substringBefore("%")
                ),
                parseDouble(dataActual[2][1].substringBefore("%")),
                parseDouble(dataActual[3][1].substringBefore("%")),
                parseDouble(dataActual[4][1].substringBefore("%")),
                stringDateToJavaData(dataActual[4][1].substringAfter("on "))!!,
                parseDouble(dataActual[5][1].substringBefore("%")),
                stringDateToJavaData(dataActual[5][1].substringAfter("on "))!!,
                parseDouble(dataActual[6][1]),
                parseInt(dataActual[7][1]),
                parseInt(dataActual[8][1]),
                parseDouble(dataActual[9][1]),
                parseInt(dataActual[10][1]),
                parseInt(dataActual[11][1])
            )
        }catch (e: Exception){
            Log.d("EXCEPTION", e.stackTraceToString())
            return null
        }
    }

}

fun main() {
    val start = System.currentTimeMillis()
    println(Quotes().getETFQuote("SPY"))
    val end = System.currentTimeMillis()

    println("Time took: ${end - start}")
}