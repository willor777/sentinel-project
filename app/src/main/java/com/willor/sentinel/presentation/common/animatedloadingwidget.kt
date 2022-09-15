package com.willor.sentinel.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*
import com.willor.sentinel.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun AnimatedLoadingWidget(
    conditionCheck: () -> Boolean,
    onConditionSuccess: () -> Unit = {},
    maxTime: Long = 5_000,
    onMaxTimeReached: () -> Unit = {},
    lottieResourceId: Int? = null,
    infoDisplay: @Composable () -> Unit = {},
){

    // TODO If you load this to a library, remove this,
    //  Or include the resource in the library upload
    val resId = lottieResourceId ?: R.raw.loading_darker

    val showAnimation = remember { mutableStateOf(true)}

    rememberCoroutineScope().launch {
        val startTime = System.currentTimeMillis()

        // Only loop until max time is reached
        while (System.currentTimeMillis() - startTime < maxTime){

            // Check condition
            if (conditionCheck()){

                // End animation and execute onConditionSuccess callback
                onConditionSuccess()
                break
            }

            // sleep .25s
            delay(250)
        }

        // End animation
        showAnimation.value = false

        onMaxTimeReached()
    }

    AnimationDisplay(show = showAnimation.value, resId) {
        infoDisplay()
    }

}


@Composable
private fun AnimationDisplay(
    show: Boolean,
    lottieResourceId: Int,
    infoDisplay: @Composable () -> Unit,
){
    if (show){
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(lottieResourceId)
        )

        val progress by animateLottieCompositionAsState(
            composition = composition, restartOnPlay = true,
            iterations = LottieConstants.IterateForever
        )

        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            LottieAnimation(composition = composition, progress = progress,
                modifier = Modifier.fillMaxHeight(.9f))
            infoDisplay()
        }
    }
}













