package com.willor.sentinel.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DisabledByDefault
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.willor.lib_data.utils.printToDEBUGTEMP
import com.willor.sentinel.ui.theme.Sizes


@Composable
fun SentinelWatchlistSimple(
    tickersList: List<String>,
    onTickerClicked: (ticker: String) -> Unit = {},
    onRemoveTickerClicked: (ticker: String) -> Unit = {}
){
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_EDGE_PADDING)
            .background(MaterialTheme.colorScheme.tertiary)
    ){

        Row(Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){
            Text(
                text = "Sentinel Watchlist",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                color = Color.White
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ){
            items(tickersList.size){tickerlistIndex ->
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            onTickerClicked(tickersList[tickerlistIndex])
                        },
                    contentAlignment = Alignment.BottomEnd
                ){
                    SingleTextChip(
                        text = tickersList[tickerlistIndex],
                        textSize = MaterialTheme.typography.titleLarge.fontSize.value.toInt()
                    )

                    // TODO Need to have a button that can remove ticker from sentinel watchlist
                    IconButton(
                        modifier = Modifier.size(35.dp, 35.dp),
                        onClick = {
                            printToDEBUGTEMP("Remove ${tickersList[tickerlistIndex]} clicked")
                            onRemoveTickerClicked(tickersList[tickerlistIndex])
                        }
                    ) {
                        Icon(Icons.Filled.DisabledByDefault, "remove", tint = Color.White)
                    }
                }

            }
        }

    }
}