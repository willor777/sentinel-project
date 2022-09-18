package com.willor.lib_data

import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader


object SymbolDataHelper{

    private val locTAG = SymbolDataHelper::class.java.name

    private var alphabeticalIndexLocations: MutableMap<Char, Int>? = null


    // TODO Delete this eventually -- Attempting to replace it with Better list
    /**
     * Loads a CSV file containing rows of data in format...
     *
     *      - Symbol, Name, Last Sale, Net Change, % Change, Market Cap,
     *      Country, IPO Year, Volume, Sector, Industry
     */
//    fun loadSyms(): List<List<String>>?{
//
//        val fn = "res/raw/tickers.csv"
//        val inputStream = this::class.java.classLoader?.getResourceAsStream(fn)
//        val csvReader = CSVReader(BufferedReader(InputStreamReader(inputStream)))
//
//        val outLines = mutableListOf<List<String>>()
//
//        try{
//            while(true){
//                val nextLine = csvReader.readNext()
//                if (nextLine == null){
//                    break
//                }
//
//                val row = mutableListOf<String>()
//
//                row.addAll(nextLine.toList())
//                outLines.add(row)
//            }
//            return outLines.toList()
//        } catch (e: Exception){
//            Log.d("INFO", "$locTAG.loadSyms() Triggred an exception." +
//                    "\n${e.stackTraceToString()}")
//            return null
//        }
//    }

    fun loadsyms(): List<List<String>>?{

        val fn = "res/raw/equity_and_etf_symbols.json"
        val stream = this::class.java.classLoader?.getResourceAsStream(fn)
        val reader = BufferedReader(InputStreamReader(stream))

        val txt = reader.readText()

        val jsonObj = Gson().fromJson<Map<String, List<List<String>>>>(txt, Map::class.java)



        return buildAlphabeticalIndexMap(jsonObj["data"]!!)
    }


    private fun buildAlphabeticalIndexMap(data: List<List<String>>): List<List<String>>{
        if (alphabeticalIndexLocations != null){
            return data
        }

        val alphabetIndexLocs = mutableMapOf<Char, Int>()

        // sort list
        var curChar: Char = '0'
        val sortedData = data.sortedBy {
            it[0]
        }

        sortedData.forEachIndexed { index, strings ->
            val (ticker,_, _) = strings

            if (ticker[0] != curChar){
                curChar = ticker[0]
                alphabetIndexLocs[curChar] = index
            }
        }

        alphabeticalIndexLocations = alphabetIndexLocs
        return sortedData
    }


    fun findIndexLocationForChar(character: Char): Int?{
        return alphabeticalIndexLocations?.get(character)
    }

}
