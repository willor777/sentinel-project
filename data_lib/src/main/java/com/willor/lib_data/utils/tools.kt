package com.willor.lib_data.utils


import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch


fun <T> Flow<T>.handleErrorsToLog(): Flow<T> =
    catch { exception ->
        Log.d(
            "EXCEPTION", "Exception Caught by Flow.handleErrorsToLog()\n" +
                    exception.stackTraceToString()
        )
    }


/**
 * Prints msg to Logcat under "DEBUGTEMP" tag
 */
fun printToTestingLog(msg: Any?){
    Log.d("TESTING", msg?.toString() ?: "NULL")
}

