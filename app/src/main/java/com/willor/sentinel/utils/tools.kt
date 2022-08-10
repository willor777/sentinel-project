package com.willor.sentinel.utils

import androidx.compose.ui.graphics.Color
import com.willor.sentinel.ui.theme.MyColors
import kotlinx.coroutines.*


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
    repeatOnFailure: Boolean = true,
    task: suspend () -> Unit
): Job {
    return scope.launch(dispatcher){
        while(true){
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
        while(true){
            var success = task()
            if (!success){
                task()
            }
            delay(delayTime)
        }
    }
}

fun determineColorForPosOrNegValue(value: Double): Color {
    return when{
        value < 0.0 ->{
            MyColors.RedColor
        }

        value > 0.0 ->{
            MyColors.GreenColor
        }

        else ->{
            Color.White
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
        "$ +${dollarChange.toUSDollarString()} (% +${pctChange.toUSDollarString()})"
    }else{
        "$ ${dollarChange.toUSDollarString()} (%${pctChange.toUSDollarString()})"

    }
}