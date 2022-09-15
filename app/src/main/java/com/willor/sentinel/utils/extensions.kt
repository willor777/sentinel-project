package com.willor.sentinel.utils

import java.text.SimpleDateFormat
import java.util.*


fun Double.toTwoDecimalPlacesString(): String{
    var strValue = this.toString()

    var isNegative = false
    if (this < 0){
        isNegative = true
        strValue = strValue.replace("-", "")
    }

    var (dollar, cents) = strValue.split(".")

    var dollarFormatted = dollar.reversed().chunked(3).joinToString(",").reversed()
    var centsFormatted = cents.padEnd(2, '0').substring(0,2)

    return if (isNegative){
        " -$dollarFormatted.$centsFormatted"
    }else{
        " $dollarFormatted.$centsFormatted"
    }
}


fun Double.toTwoDecimalPlaceString(): String{
    if (!this.toString().contains(".")){
        return this.toString()
    }
    val split = this.toString().split(".")

    return if (split[1].length < 3){
        "${split[0]}.${split[1].padEnd(2, '0')}"
    }
    else{
        "${split[0]}.${split[1].substring(0, 2)}"
    }
}


fun Int.toCommaString(): String{
    var strValue = this.toString()

    var isNegative = false
    if (this < 0){
        isNegative = true
        strValue = strValue.replace("-", "")
    }

    var strValueFormatted = strValue.reversed().chunked(3).joinToString(",").reversed()

    return if (isNegative){
        "-$strValueFormatted"
    }else{
        "$strValueFormatted"
    }
}


fun Long.toCommaString(): String{
    var strValue = this.toString()

    var isNegative = false
    if (this < 0){
        isNegative = true
        strValue = strValue.replace("-", "")
    }

    var strValueFormatted = strValue.reversed().chunked(3).joinToString(",").reversed()

    return if (isNegative){
        "-$strValueFormatted"
    }else{
        "$strValueFormatted"
    }
}


fun Long.toDateString(): String{

    try{
        val d = Date(this)
        return SimpleDateFormat("MM/dd/yy hh:mm aa").format(d)
    }catch (e: Exception){
        return "Unknown"
    }

}


fun Date.formatToMMDDYYYYString(): String{
    return SimpleDateFormat("MM/DD/YYYY").format(this)
}
