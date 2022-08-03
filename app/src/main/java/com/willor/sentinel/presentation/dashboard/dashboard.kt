package com.willor.sentinel.presentation.dashboard

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(start = true)
@Composable
fun Dashboard(
    navigator: DestinationsNavigator,
    viewModel: DashboardViewModel = hiltViewModel()
){

    Text(text = "Welcome To Dashboard :D")

    Button(
        onClick = {
            viewModel.testAdvancedChart()
        }
    ){
        Text("Click Me")
    }

}