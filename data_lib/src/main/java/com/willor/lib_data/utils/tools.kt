package com.willor.lib_data.utils

import android.util.Log
import com.willor.ktstockdata.common.Log
import com.willor.ktstockdata.common.d
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch


fun <T> Flow<T>.handleErrorsToLog(): Flow<T> =
    catch {exception ->
        Log.d("EXCEPTION", "Exception Caught by Flow.handleErrorsToLog()\n" +
                 exception.stackTraceToString())
    }


// TODO Delete this, I probably won't use it
fun <T> retryOnNullReturn(max: Int = 3, delay: Int = 1, f: ()->T): T?{
    var attempts = 0
    while (attempts < max){

        val result = f()
        if (result != null){
            return result
        }

        attempts += 1
        printToDEBUG("Sleeping for: ${delay * attempts.toDouble() * 1000.0}")
        Thread.sleep(delay * attempts.toLong())
    }
    return null
}





fun printToDEBUG(msg: String){
    Log.d("DEBUG", msg)
}

