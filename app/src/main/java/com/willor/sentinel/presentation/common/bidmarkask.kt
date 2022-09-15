package com.willor.sentinel.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.willor.sentinel.utils.toTwoDecimalPlacesString


@Composable
fun BidMarkAsk(
    bidPrice: Double,
    askPrice: Double,
){
    Column(
        Modifier.fillMaxWidth()
            .wrapContentHeight()
    ){

        // Mark price header
        MarkHeader()

        BidAskMarkPrice(bidPrice = bidPrice, askPrice = askPrice)
    }
}


@Composable
private fun MarkHeader(){
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            "mark",
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
            color = MaterialTheme.colorScheme.onTertiary,
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
        )
    }
}


@Composable
private fun BidAskMarkPrice(
    bidPrice: Double,
    askPrice: Double
){

    val mark = (bidPrice + askPrice) / 2.0

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){

        // Bid price
        Text(
            text = "bid $${bidPrice.toTwoDecimalPlacesString()}",
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
            color = MaterialTheme.colorScheme.onTertiary,
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
        )

        // Mark price
        Text(
            text = "$${mark.toTwoDecimalPlacesString()}",
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
            color = MaterialTheme.colorScheme.onTertiary,
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
        )

        // Ask price
        Text(
            text = "$${askPrice.toTwoDecimalPlacesString()} ask",
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
            color = MaterialTheme.colorScheme.onTertiary,
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
        )
    }
}

