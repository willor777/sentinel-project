package com.willor.sentinel.presentation.dash.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.ktstockdata.misc_data.dataobjects.MajorFuturesData
import com.willor.sentinel.presentation.common.BasicAssetDisplayChip

// TODO on click should take user to quote page and display quote info
@Composable
fun FuturesDisplay(
    navigator: DestinationsNavigator,
    majorFuturesData: MajorFuturesData
){


    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ){
        item{
            BasicAssetDisplayChip(
                assetName = "S&P 500 Future",
                lastPrice = majorFuturesData.sp500Future!!.lastPrice,
                dollarChange = majorFuturesData.sp500Future!!.changeDollar,
                pctChange = majorFuturesData.sp500Future!!.changePercent,
                onClick = {
                    Log.d("INFO", "Sp500 clicked")
                }
            )
        }

        item{
            BasicAssetDisplayChip(
                assetName = "Nasdaq Future",
                lastPrice = majorFuturesData.nasdaqFuture!!.lastPrice,
                dollarChange = majorFuturesData.nasdaqFuture!!.changeDollar,
                pctChange = majorFuturesData.nasdaqFuture!!.changePercent
            )
        }

        item{
            BasicAssetDisplayChip(
                assetName = "DJI Future",
                lastPrice = majorFuturesData.dowFuture!!.lastPrice,
                dollarChange = majorFuturesData.dowFuture!!.changeDollar,
                pctChange = majorFuturesData.dowFuture!!.changePercent,
            )
        }

        item{
            BasicAssetDisplayChip(
                assetName = "Russel 2000 Future",
                lastPrice = majorFuturesData.russel2000Future!!.lastPrice,
                dollarChange = majorFuturesData.russel2000Future!!.changeDollar,
                pctChange = majorFuturesData.russel2000Future!!.changePercent
            )
        }
    }
}