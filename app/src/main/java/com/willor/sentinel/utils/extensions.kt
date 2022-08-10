package com.willor.sentinel.utils


fun Double.toUSDollarString(): String{
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
        "-$dollarFormatted.$centsFormatted"
    }else{
        "$dollarFormatted.$centsFormatted"
    }
}


fun Double.toTwoDecimalPlaceString(): String{
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


fun main() {

    val test = 133_333_333_333_333

    println(test.toCommaString())

}