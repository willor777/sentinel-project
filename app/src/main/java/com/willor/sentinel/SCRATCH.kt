package com.willor.sentinel


fun main() {

    val splt = {s: String ->
        var indexOne: String = ""
        var indexTwo: String = ""

        var atSplit = false
        for (n in 0..s.lastIndex){
            if (s[n].isDigit()){
                indexOne += s[n]
            }else{
                indexTwo += s[n]
            }
        }

        mutableListOf(indexOne, indexTwo)
    }

    val test = "2733455M"

    println(splt(test))


}