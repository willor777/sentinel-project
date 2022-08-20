package com.willor.sentinel.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*
import com.willor.sentinel.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max


@Composable
fun AnimatedLoadingScreen(
    conditionCheck: () -> Boolean,
    onConditionSuccess: () -> Unit = {},
    maxTime: Long = 10_000,
    onMaxTimeReached: () -> Unit = {},
    infoDisplay: @Composable () -> Unit = {}
){

    val showAnimation = remember { mutableStateOf(true)}

    rememberCoroutineScope().launch {
        val startTime = System.currentTimeMillis()
        while (true){
            if (conditionCheck()){
                showAnimation.value = false
                onConditionSuccess()
                break
            }

            if (System.currentTimeMillis() - startTime > maxTime){
                onMaxTimeReached()
                break
            }

            delay(250)
        }
    }

    AnimationDisplay(show = showAnimation.value) {
        infoDisplay()
    }

}


@Composable
fun AnimationDisplay(
    show: Boolean,
    infoDisplay: @Composable () -> Unit
){
    if (show){

        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.loading_darker)
        )

        val progress by animateLottieCompositionAsState(
            composition = composition, restartOnPlay = true,
            iterations = LottieConstants.IterateForever
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            LottieAnimation(composition = composition, progress = progress,
                modifier = Modifier.fillMaxHeight(.9f))
            infoDisplay()
        }
    }
}













