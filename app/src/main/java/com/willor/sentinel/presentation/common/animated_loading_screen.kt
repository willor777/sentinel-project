package com.willor.sentinel.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*
import com.willor.sentinel.R


/**
 * Displays a Loading Animation until the showAnimation param is changed. Also accepts a
 * Composable content to display below the animation.
 *
 * Note-- Animation is set to take up .9f of the fullscreen height
 */
@Composable
fun LoadingDataAnimation(showAnimation: Boolean, loadingText: @Composable ()->Unit){

    if (showAnimation){
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
            loadingText()
        }
    }
}
