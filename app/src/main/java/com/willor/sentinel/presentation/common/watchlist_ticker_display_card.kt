package com.willor.sentinel.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.willor.ktstockdata.watchlists_data.dataobjects.Ticker
import com.willor.sentinel.ui.theme.Sizes
import com.willor.sentinel.utils.*


@Composable
fun WatchlistTickerDisplayCard(
    ticker: Ticker,
    onCardClicked: (ticker: String) -> Unit,
    onAddButtonClicked: (ticker: String) -> Unit
) {

    Card(
        backgroundColor = MaterialTheme.colorScheme.secondary,
        shape = RoundedCornerShape(Sizes.ROUNDED_CORNER_NORMAL),
        elevation = Sizes.ELEVATION_FOR_CARD,

    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(MaterialTheme.colorScheme.secondary)
                .clickable {
                    onCardClicked(ticker.ticker)
                },
        ) {

            TopRow(ticker = ticker)

            Spacer(Modifier.height(Sizes.CONTENT_SPACER_NORMAL))

            VolumeRow(ticker = ticker)

            AvgVolumeRow(ticker = ticker)

            VolumeDifference(ticker = ticker)

            VolumeRatio(ticker = ticker)

            MarketCap(ticker = ticker)

            Spacer(Modifier.height(Sizes.CONTENT_SPACER_NORMAL))

            AddButton(
                ticker = ticker,
                onAddButtonClicked = { onAddButtonClicked(ticker.ticker) }
            )
        }
    }
}


/**
 * Displays [ticker lastPrice dollarChange, percentChange]
 */
@Composable
fun TopRow(
    ticker: Ticker
){
    val colorForGainLoss = determineColorForPosOrNegValue(ticker.changeDollar)

    // Row: SYMBOL and Current Price and Price Change
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Sizes.HORIZONTAL_PADDING_NORMAL, Sizes.VERTICAL_PADDING_SMALL),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {

        // Stock Symbol
        Text(
            ticker.ticker,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = Color.White
        )

        // Last price
        Text(
            "$${ticker.lastPrice.toUSDollarString()}",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = Color.White
        )

        // Gain loss
        Text(
            buildChangeDollarChangePercentDisplayString(ticker.changeDollar,
                ticker.changePercent),
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = colorForGainLoss
        )
    }
}


@Composable
fun VolumeRow(
    ticker: Ticker
){
    // Volume row
    Row(
        Modifier
            .fillMaxWidth()
            .padding(Sizes.HORIZONTAL_PADDING_NORMAL, Sizes.VERTICAL_PADDING_SMALL),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            "Volume",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = Color.White
        )

        Text(
            ticker.volume.toCommaString(),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = Color.White
        )
    }

}


@Composable
fun AvgVolumeRow(
    ticker: Ticker
){
    // Avg Volume row
    Row(
        Modifier
            .fillMaxWidth()
            .padding(Sizes.HORIZONTAL_PADDING_NORMAL, Sizes.VERTICAL_PADDING_SMALL),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            "Avg Volume",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = Color.White
        )

        Text(
            ticker.volumeThirtyDayAvg.toCommaString(),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = Color.White
        )
    }

}


@Composable
fun VolumeDifference(
    ticker: Ticker
){
    val volDiffColor = determineColorForPosOrNegValue(
        (ticker.volume - ticker.volumeThirtyDayAvg).toDouble()
    )

    // Volume difference
    Row(
        Modifier
            .fillMaxWidth()
            .padding(Sizes.HORIZONTAL_PADDING_NORMAL, Sizes.VERTICAL_PADDING_SMALL),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            "Volume Difference",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = Color.White
        )

        Text(
            (ticker.volume - ticker.volumeThirtyDayAvg).toCommaString(),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = volDiffColor
        )
    }

}


@Composable
fun VolumeRatio(
    ticker: Ticker
){
    val volDiffColor = determineColorForPosOrNegValue(
        (ticker.volume - ticker.volumeThirtyDayAvg).toDouble()
    )

    // Volume Ratio
    Row(
        Modifier
            .fillMaxWidth()
            .padding(Sizes.HORIZONTAL_PADDING_NORMAL, Sizes.VERTICAL_PADDING_SMALL),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            "Vol / AvgVol Ratio",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = Color.White
        )

        Text(
            (ticker.volume.toDouble() / ticker.volumeThirtyDayAvg.toDouble())
                .toTwoDecimalPlaceString(),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = volDiffColor
        )
    }
}


@Composable
fun MarketCap(
    ticker: Ticker
){
    // Market Cap
    Row(
        Modifier
            .fillMaxWidth()
            .padding(Sizes.HORIZONTAL_PADDING_NORMAL, Sizes.VERTICAL_PADDING_SMALL),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            "Market Cap",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = Color.White
        )

        Text(
            ticker.marketCap.toCommaString(),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = Color.White
        )
    }
}


@Composable
fun AddButton(
    ticker: Ticker,
    onAddButtonClicked: (Ticker) -> Unit
){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(Sizes.HORIZONTAL_PADDING_NORMAL, Sizes.VERTICAL_PADDING_SMALL),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){

        Text(
            "Add Ticker To Watchlist",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = Color.White
        )

        IconButton(
            onClick = { onAddButtonClicked(ticker) }
        ){
            Icon(Icons.Filled.AddCircle, "add circle", tint = Color.White)
        }

    }
}


