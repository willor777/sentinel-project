package com.willor.sentinel.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.willor.sentinel.ui.theme.MyColors
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Accepts a trailing lambda 'task' that will launch immediately,
 * then repeatedly after 'delayTime'. 'task' is launched on the given 'scope' on the given
 * 'dispatcher' thread.
 *
 * :param: delayTime - Time to wait in between task execution
 * :param: scope - Scope to launch the coroutine
 * :param: dispatcher - Thread to launch coroutine on
 *
 * Returns a Job object which is the coroutine itself allowing it to be cancelled.
 */
fun periodicCoroutine(
    delayTime: Long,
    scope: CoroutineScope,
    dispatcher: CoroutineDispatcher,
    task: suspend () -> Unit
): Job {
    return scope.launch(dispatcher){
        while(this.isActive){
            task()
            delay(delayTime)
        }
    }
}


/**
 * Accepts a trailing lambda 'task' that will launch immediately,
 * then repeatedly after 'delayTime'. 'task' must return a Boolean signaling success of task. If
 * task fails, it will immediately repeat once before the delayTime pause
 * 'task' is launched on the given 'scope' on the given
 * 'dispatcher' thread.
 *
 * :param: delayTime - Time to wait in between task execution
 *
 * :param: scope - Scope to launch the coroutine
 *
 * :param: dispatcher - Thread to launch coroutine on
 *
 * Returns a Job object which is the coroutine itself allowing it to be cancelled.
 */
fun periodicCoroutineRepeatOnFailure(
    delayTime: Long,
    scope: CoroutineScope,
    dispatcher: CoroutineDispatcher,
    task: suspend () -> Boolean
): Job {
    return scope.launch(dispatcher){
        while(this.isActive){
            val success = task()

            // If task failed, delay only 2s
            if (!success){
                delay(2000)
            }

            // Delay for full time
            else{
                delay(delayTime)
            }
        }
    }
}


/**
 * Returns the Defined Green (pos) or Red (neg) color for the given value
 */
@Composable
fun determineColorForPosOrNegValue(value: Double): Color {
    return when{
        value < 0.0 ->{
            MyColors.LossLightMode
        }

        value > 0.0 ->{
            MyColors.GainLightMode
        }

        else ->{
            MaterialTheme.colorScheme.onTertiary
        }
    }
}


@Composable
fun determineColorForPutCallRatio(value: Double): Color{
    return when {
        value > 1.15 -> {
            MyColors.LossLightMode
        }
        value < 0.7 -> {
            MyColors.GainLightMode
        }
        else -> {
            MaterialTheme.colorScheme.onTertiary
        }
    }
}


/**
 * Returns a string in the format...
 *
 * - $ dollarChange (% pctChange)
 * - Example...
 *      - $1.00 (% 0.45)
 */
fun buildChangeDollarChangePercentDisplayString(dollarChange: Double, pctChange: Double): String{

    return if (dollarChange > 0){
        "$ +${dollarChange.toTwoDecimalPlacesString()} (% +${pctChange.toTwoDecimalPlacesString()})"
    }else{
        "$ ${dollarChange.toTwoDecimalPlacesString()} (%${pctChange.toTwoDecimalPlacesString()})"

    }
}


/**
 * Determines the ratio of two Int and rounds off to 2 decimal places
 */
fun getRatio(valOne: Int, valTwo: Int): String{
    return (valOne.toDouble() / valTwo.toDouble()).toTwoDecimalPlaceString()
}


/**
 * Returns Date formatted to MM/DD/YYYY String... Example: 01/04/1999
 */
fun formatDateToMMDDYYYYString(date: Date?): String{
    if (date == null){return "N/A"}

    return SimpleDateFormat("MM/dd/YYYY").format(date)
}










