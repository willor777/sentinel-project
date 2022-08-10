package com.willor.sentinel.presentation.sentinel_active

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun SentinelActiveScreen(
    navigator: DestinationsNavigator,
    viewmodel: SentinelActiveViewModel = hiltViewModel()
){

}