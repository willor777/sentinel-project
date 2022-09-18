package com.willor.sentinel.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.sentinel.R
import com.willor.sentinel.presentation.destinations.DashboardScreenDestination
import kotlinx.coroutines.delay


@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator
){

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.potion_splash)
    )

    val progress by animateLottieCompositionAsState(
        composition
    )

    LaunchedEffect(key1 = true) {
//        delay(3500)
        delay(500)
        navigator.navigate(DashboardScreenDestination)
    }


    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black)
    ){

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LottieAnimation(composition = composition, progress = progress,
            modifier = Modifier.fillMaxHeight(.8f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    "Digital Alchemy Software",
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    "Project Sentinel",
                    fontSize = 36.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }
    }
}
