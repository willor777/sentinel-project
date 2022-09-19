package com.willor.sentinel.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DisabledByDefault
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.willor.lib_data.data.local.preferences.SentinelSettings
import com.willor.lib_data.utils.printToTestingLog
import com.willor.sentinel.ui.theme.Sizes


@Composable
fun SentinelWatchlistLazyRow(
    sentinelSettings: State<SentinelSettings?>? = null,
    tickerList: List<String> = listOf(),
    onTickerClicked: (ticker: String) -> Unit = {},
    onRemoveTickerClicked: (ticker: String) -> Unit = {}
){

    // Pull watchlist either from settings or hardcoded list
    val tickList = sentinelSettings?.value?.currentWatchlist ?: tickerList

    printToTestingLog("Ticker List: ${tickList}, Sentinel Settings: $sentinelSettings")

    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_EDGE_PADDING)
    ){

        Row(Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
            Text(
                text = "Sentinel Watchlist",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ){
            items(tickList.size){ ind ->
                Box(
                    modifier = Modifier
                        .wrapContentSize(),
                    contentAlignment = Alignment.BottomEnd
                ){
                    SingleTextChip(
                        text = tickList[ind],
                        textSize = MaterialTheme.typography.titleLarge.fontSize.value.toInt(),
                        onClick = {
                            onTickerClicked(tickList[ind])
                        }
                    )

                    // TODO Need to have a button that can remove ticker from sentinel watchlist
                    Icon(
                        Icons.Filled.DisabledByDefault,
                        "remove",
                        tint = Color.Black,
                        modifier = Modifier.background(Color.White).wrapContentSize()
                            .clickable {
                                onRemoveTickerClicked(tickList[ind])
                        }
                    )
                }

            }
        }

    }
}
