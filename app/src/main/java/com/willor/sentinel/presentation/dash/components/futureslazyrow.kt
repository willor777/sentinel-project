package com.willor.sentinel.presentation.dash.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.willor.ktstockdata.marketdata.dataobjects.MajorFuturesData
import com.willor.sentinel.presentation.common.BasicAssetChip
import com.willor.sentinel.ui.theme.Sizes


// TODO on click should take user to quote page and display quote info
@Composable
fun FuturesDisplay(
    majorFuturesData: MajorFuturesData
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_PADDING_NORMAL)
    ) {

        LazyRow(
            Modifier.padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_EDGE_PADDING),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                BasicAssetChip(
                    assetName = "S&P 500 Future",
                    lastPrice = majorFuturesData.sp500Future!!.lastPrice,
                    dollarChange = majorFuturesData.sp500Future!!.changeDollar,
                    pctChange = majorFuturesData.sp500Future!!.changePercent,
                    onClick = {
                        Log.d("INFO", "Sp500 clicked")
                    }
                )
            }

            item {
                BasicAssetChip(
                    assetName = "Nasdaq Future",
                    lastPrice = majorFuturesData.nasdaqFuture!!.lastPrice,
                    dollarChange = majorFuturesData.nasdaqFuture!!.changeDollar,
                    pctChange = majorFuturesData.nasdaqFuture!!.changePercent
                )
            }

            item {
                BasicAssetChip(
                    assetName = "DJI Future",
                    lastPrice = majorFuturesData.dowFuture!!.lastPrice,
                    dollarChange = majorFuturesData.dowFuture!!.changeDollar,
                    pctChange = majorFuturesData.dowFuture!!.changePercent,
                )
            }

            item {
                BasicAssetChip(
                    assetName = "Russel 2000 Future",
                    lastPrice = majorFuturesData.russel2000Future!!.lastPrice,
                    dollarChange = majorFuturesData.russel2000Future!!.changeDollar,
                    pctChange = majorFuturesData.russel2000Future!!.changePercent
                )
            }
        }
    }
}
