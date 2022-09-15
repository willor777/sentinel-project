package com.willor.lib_data

import android.util.Log
import com.opencsv.CSVReader
import java.io.BufferedReader
import java.io.InputStreamReader


object StockSymbolsLoader{

    private val locTAG = StockSymbolsLoader::class.java.name

    /**
     * Loads a CSV file containing rows of data in format...
     *
     *      - Symbol, Name, Last Sale, Net Change, % Change, Market Cap,
     *      Country, IPO Year, Volume, Sector, Industry
     */
    fun loadSyms(): List<List<String>>?{

        val fn = "res/raw/tickers.csv"
        val inputStream = this::class.java.classLoader?.getResourceAsStream(fn)
        val csvReader = CSVReader(BufferedReader(InputStreamReader(inputStream)))

        val outLines = mutableListOf<List<String>>()

        try{
            while(true){
                val nextLine = csvReader.readNext()
                if (nextLine == null){
                    break
                }

                val row = mutableListOf<String>()

                row.addAll(nextLine.toList())
                outLines.add(row)
            }
            return outLines.toList()
        } catch (e: Exception){
            Log.d("INFO", "$locTAG.loadSyms() Triggred an exception." +
                    "\n${e.stackTraceToString()}")
            return null
        }
    }

}