package com.willor.sentinel.presentation.sentinel.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.willor.lib_data.data.local.local_preferences.SentinelSettings
import com.willor.sentinel.presentation.common.CurrentSettingsLabelValueRow
import com.willor.sentinel.presentation.common.SentinelWatchlistSimple
import com.willor.sentinel.ui.theme.Sizes
import com.willor.sentinel.utils.toDateString


@Composable
fun SentinelInfoDisplay(
    sentinelSettings: State<SentinelSettings?>,
    sentinelInfoOnClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
//            .height(220.dp)
            .background(color = MaterialTheme.colorScheme.tertiary)
            .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_PADDING_NORMAL)
            .clickable { sentinelInfoOnClick() }
    ) {

        TitleHeader()

//        Spacer(Modifier.height(Sizes.CONTENT_SPACER_NORMAL))

        LastScanTime(sentinelSettings = sentinelSettings)

//        Spacer(Modifier.height(Sizes.CONTENT_SPACER_SMALL))

        ScanInterval(sentinelSettings = sentinelSettings)

//        Spacer(Modifier.height(Sizes.CONTENT_SPACER_SMALL))

        CurrentScannerAlgo(sentinelSettings = sentinelSettings)
    }
}



@Composable
fun TitleHeader(){
    // Header Text
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_PADDING_NORMAL)
            .wrapContentSize(),
        horizontalArrangement = Arrangement.Center
    ) {

        Text(
            "Sentinel Info",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight
        )
    }
}


@Composable
fun LastScanTime(
    sentinelSettings: State<SentinelSettings?>
){
    CurrentSettingsLabelValueRow(
        label = "Last Scan Time:",
        value = sentinelSettings.value?.lastScan?.toDateString() ?: ""
    )
}


@Composable
fun ScanInterval(
    sentinelSettings: State<SentinelSettings?>
){

    val interval = if (sentinelSettings.value != null){
        "${sentinelSettings.value!!.scanInterval / 1000} seconds"
    }else{
        "Not Set"
    }

    CurrentSettingsLabelValueRow(
        label = "Scan Interval:",
        value = interval
    )
}


@Composable
fun CurrentScannerAlgo(
    sentinelSettings: State<SentinelSettings?>
){
    CurrentSettingsLabelValueRow(
        label = "Current Scanner: ",
        value = sentinelSettings.value?.currentScanAlgo ?: "Not Set"
    )
}


@Composable
fun SentinelWatchlist(
    sentinelSettings: State<SentinelSettings?>
){
    SentinelWatchlistSimple(tickersList = sentinelSettings.value?.currentWatchlist ?: listOf())
}









