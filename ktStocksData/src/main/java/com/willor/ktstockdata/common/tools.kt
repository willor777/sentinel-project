package com.willor.ktstockdata.common

import java.lang.Exception
import java.util.logging.Logger
import kotlin.math.pow
import kotlin.math.sqrt


val Log: Logger = Logger.getGlobal()


fun Logger.d(tag: String, msg: String){
    Logger.getGlobal().warning("$tag, $msg")
}

const val MOZILLA_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) " +
        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36"



/** Creates the URL with query parameters added to the end of it (from Map<String,*>)
 *
 * Map of parameters is converted to a params string '?key=value&key=value'
 */
internal fun addParamsToUrl(urlString: String, params: Map<String, *>): String {

    var queryString = "?"

    for (k: String in params.keys) {
        queryString += "$k=${params[k].toString()}&"
    }

    return urlString + queryString.substring(0, queryString.length - 2)
}


/**
 * Returns one of 6 of the most used user-agents randomly
 */
internal fun getRandomUserAgent(): String{
    val userAgents = listOf(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                " Chrome/58.0.3029.110 Safari/537.36",
   "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0",

   "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0; Trident/5.0)",

    "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0; MDDCJS)",

    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
   " Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393",

   "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)"
    )

    return userAgents[(0..5).random()]
}


/**
 * Attempts to parse Double from a String. Will remove all commas and spaces. On failure will
 * return 0.0 as default
 */
internal fun parseDouble(str: String): Double{

    return try{
        var s = str.replace(" ", "")
        if (!(s[s.lastIndex].isDigit()) && s.contains("-")){
            return 0.0
        }
        s.replace(",", "")
            .toDouble()
    } catch (e: Exception){
        Log.d("EXCEPTION", "tools.parseDouble() Failed...Returning Default 0.0\n"
                + e.stackTraceToString())
        println("DEFAULT")
        return 0.0
    }
}


/**
 * Attempts to parse Double from a String, Will remove all commas and spaces. On failure will
 * return 0 as default
 */
internal fun parseLongFromBigAbbreviatedNumbers(str: String): Long{
    try{
        // Remove spaces
        val s = str.replace(" ", "").replace(",", "")

        // Check for non abbreviated number
        if(!s[s.lastIndex].isLetter()){
            return s.replace(",", "").toLong()
        }

        val splitString = s.split(".")
        val previxValue = splitString[0]

        when {
            s.contains("M") -> {
                return (
                        previxValue + splitString[1]
                            .replace("M", "")
                            .padEnd(3, '0') + "000"
                        ).toLong()
            }

            s.contains("B") -> {
                return (
                        previxValue + splitString[1]
                            .replace("B", "")
                            .padEnd(3, '0') + "000000"
                        ).toLong()
            }
            s.contains("T") -> {
                return (
                        previxValue + splitString[1]
                            .replace("T", "")
                            .padEnd(3, '0') + "000000000"
                        ).toLong()
            }
            s.contains("k") -> {
                return (
                        previxValue + splitString[1]
                            .replace("k", "")
                            .padEnd(3, '0')
                        ).toLong()
            }
        }

        return 0
    }catch (e: Exception){
        Log.d("EXCEPTION", "tools.parseLongFromBigAbbreviatedNumbers() Failed to parse" +
                " Long. Returning 0.0 by default\n" + e.stackTraceToString())
        return 0
    }
}


/**
 * Attempts to parse Int from a String. Will remove all commas and spaces. On failure will
 * return 1 as default
 */
internal fun parseInt(s: String): Int{
    return try{
        s.replace(" ", "").replace(",", "").toInt()
    }catch (e: Exception){
        Log.d("EXCEPTION", "tools.parseInt() Failed to parse Int. Returning 0\n"
        + e.stackTraceToString())
        return 0
    }
}



fun calculateStandardDeviationForDoubles(l: List<Double>): Double {
    var sum = 0.0
    var standardDeviation = 0.0

    for (num in l) {
        sum += num
    }

    val mean = sum / 10

    for (num in l) {
        standardDeviation += (num - mean).pow(2.0)
    }

    return sqrt(standardDeviation / 10)
}


fun calculateAvgOfList(l: List<Double>): Double{
    return l.sum() / l.size
}


fun calculateEma(curValue: Double, prevEma: Double, window: Int, smoothingFactor: Double = 2.0
): Double{

    val k =  2.0 / (1.0 + window.toDouble())
//    val todayEma = (n * smoothingFactor) + (prevEma * (1 - smoothingFactor))

//    ema = closeToday x multiplier + emaPrevDay x (1-multiplier)

    return (curValue * k) + (prevEma * (1 - k))

}
