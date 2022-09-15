package com.willor.sentinel.presentation.quote.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.willor.ktstockdata.marketdata.dataobjects.EtfQuote
import com.willor.ktstockdata.marketdata.dataobjects.StockQuote
import com.willor.sentinel.R
import com.willor.sentinel.presentation.common.AnimatedLoadingWidget
import com.willor.sentinel.presentation.common.BidMarkAsk
import com.willor.sentinel.presentation.common.NoDataImage
import com.willor.sentinel.ui.theme.Sizes
import com.willor.sentinel.utils.*

// TODO Test tbe pre-post market changes


@Composable
fun QuoteBaseStats(
    stockQuote: State<StockQuote?>,
    etfQuote: State<EtfQuote?>,
    quoteFailed: State<Boolean>,
    onRetryClicked: () -> Unit,
){

    val showContent = remember { mutableStateOf(false)}

    if (stockQuote.value == null && etfQuote.value == null){showContent.value = false}

    // Loading animation if no content is available, but hasn't failed to load
    if (!showContent.value && !quoteFailed.value){

        // Non-Scrolling Layout for Loading Animation
        Column(
            Modifier
                .fillMaxHeight(.4f)
                .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_EDGE_PADDING)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
        ) {

            // Display loading screen until either stock or etf quote loads
            AnimatedLoadingWidget(
                conditionCheck = {
                    // Check for data success
                    stockQuote.value != null || etfQuote.value != null
                },
                onConditionSuccess = {
                    showContent.value = true
                },
                maxTime = 20_000,
                onMaxTimeReached = {

                },
                lottieResourceId = R.raw.loading_darker
            ){
                Text("Stock Info Loading")
            }
        }
    }

    // No Data if failed to load
    else if (quoteFailed.value){
        Column(
            Modifier
                .fillMaxHeight(.4f)
                .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_EDGE_PADDING)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NoDataImage(modifier = Modifier.size(150.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Icon(
                Icons.Filled.Refresh,
                "retry-for-quote",
                tint = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier.clickable {
                    onRetryClicked()
                }
            )
        }
    }

    // Show Content
    else{
        // Scrolling layout for data
        Column(
            Modifier
                .fillMaxHeight(.4f)
                .verticalScroll(rememberScrollState())
                .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_EDGE_PADDING)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
        ) {

            // Check to show either stockQuote or etf quote
            if (stockQuote.value != null){
                StockQuoteDisplay(stockQuote)
            }else if (etfQuote.value != null){
                ETFQuoteDisplay(etfQuote)
            }
        }
    }

}


// TODO Better check for "if in prepost hours". Maybe check the actual time ? :P
@Composable
private fun StockQuoteDisplay(stockQuote: State<StockQuote?>){

    val q = stockQuote.value!!
    val glColorRegular = determineColorForPosOrNegValue(q.changePctRegMarket)
    val glColorPrepost = determineColorForPosOrNegValue(q.prepostChangeDollar)
    val volDiff = q.volume - q.avgVolume
    val volDiffColor = determineColorForPosOrNegValue(volDiff.toDouble())

    // determine cur price based on afterhours / cur hours
    val curPrice = if (q.prepostChangeDollar == 0.0){q.lastPriceRegMarket} else { q.prepostPrice }


    TickerHeader(ticker = q.ticker)

    Spacer(modifier = Modifier.height(20.dp))

    BidMarkAsk(bidPrice = q.bidPrice, askPrice = q.askPrice)

    // Check for post market hours
    if (q.prepostChangeDollar != 0.0){

        LabelValueRow(
            label = "Last Price",
            value = q.lastPriceRegMarket.toTwoDecimalPlacesString(),
            labelSubscript = "Market Hours"
        )

        LabelValueRow(
            label = "Change",
            value = buildChangeDollarChangePercentDisplayString(
                q.changeDollarRegMarket, q.changePctRegMarket
            ),
            valueColor = glColorRegular,
            labelSubscript = "Market Hours"
        )

        LabelValueRow(
            label = "Last Price",
            value = q.prepostPrice.toTwoDecimalPlacesString(),
            labelSubscript = "Pre/Post"
            )

        LabelValueRow(
            label = "Change",
            value = buildChangeDollarChangePercentDisplayString(
                q.prepostChangeDollar, q.prepostChangePct
            ),
            valueColor = glColorPrepost,
            labelSubscript = "Pre/Post"
        )
    }

    // Regular market hours
    else{
        LabelValueRow(label = "Last Price", value = q.lastPriceRegMarket.toTwoDecimalPlacesString())

        LabelValueRow(
            label = "Change",
            value = buildChangeDollarChangePercentDisplayString(
                q.changeDollarRegMarket, q.changePctRegMarket
            ),
            valueColor = glColorRegular
        )
    }

    LabelValueRow(label = "Open Price", value = q.openPrice.toTwoDecimalPlacesString())

    LabelValueRow(label = "Previous Close", value = q.prevClose.toTwoDecimalPlacesString())

    LabelValueRow(label = "Volume", value = q.volume.toCommaString())

    LabelValueRow(label = "Volume", value = q.avgVolume.toCommaString(), labelSubscript = "Average")

    LabelValueRow(
        label = "Volume Difference",
        value = volDiff.toCommaString(),
        valueColor = determineColorForPosOrNegValue(volDiff.toDouble())
    )

    LabelValueRow(
        label = "Volume/Avg Ratio",
        value = getRatio(q.volume, q.avgVolume),
        valueColor = volDiffColor
    )

    Spacer(modifier = Modifier.height(20.dp))

    ValueRangeBar(
        highValue = q.daysRangeHigh,
        highLabel = "Day's High",
        curPrice = curPrice,
        lowValue = q.daysRangeLow,
        lowLabel = "Day's Low",
        topRowValue = curPrice,
        btmRowValue = q.daysRangeLow + ((q.daysRangeHigh - q.daysRangeLow) / 2.0),
        showMidPoint = true
    )

    Spacer(modifier = Modifier.height(20.dp))

    ValueRangeBar(
        highValue = q.fiftyTwoWeekRangeHigh,
        highLabel = "52wk High",
        curPrice = curPrice,
        lowValue = q.fiftyTwoWeekRangeLow,
        lowLabel = "52wk Low",
        btmRowValue = (q.fiftyTwoWeekRangeLow +
                ((q.fiftyTwoWeekRangeHigh - q.fiftyTwoWeekRangeLow) / 2.0)),
        showMidPoint = true
    )

    Spacer(modifier = Modifier.height(20.dp))

    LabelValueRow(label = "Market Cap", value = "$${q.marketCapAbbreviatedString}")
    
    LabelValueRow(
        label = "Beta",
        value = q.betaFiveYearMonthly.toString(),
        labelSubscript = "5yr Monthly"
    )
    
    LabelValueRow(label = "PE Ratio", value = q.peRatioTTM.toString(), labelSubscript = "TTM")
    
    LabelValueRow(label = "EPS", value = "$${q.epsTTM}", labelSubscript = "TTM")

    LabelValueRow(
        label = "Next Earnings",
        value = formatDateToMMDDYYYYString(q.nextEarningsDate),
    labelSubscript = "Approx."
    )

    LabelValueRow(
        label = "Dividend Yield",
        value = formatDivYield(q.forwardDivYieldValue, q.forwardDivYieldPercentage)
    )

    LabelValueRow(
        label = "Ex Dividend Date",
        value = formatDateToMMDDYYYYString(q.exDividendDate),
        labelSubscript = "Approx."
    )

    LabelValueRow(
        label = "1yr Value Est.",
        value = "$${q.oneYearTargetEstimate}",
    )

    Spacer(modifier = Modifier.height(30.dp))

}


@Composable
private fun ETFQuoteDisplay(etfQuote: State<EtfQuote?>){

    val q = etfQuote.value!!
    val glColorRegular = determineColorForPosOrNegValue(q.changePctRegMarket)
    val glColorPrepost = determineColorForPosOrNegValue(q.prepostChangeDollar)
    val volDiff = q.volume - q.avgVolume
    val volDiffColor = determineColorForPosOrNegValue(volDiff.toDouble())

    TickerHeader(ticker = q.ticker)

    Spacer(modifier = Modifier.height(20.dp))

    BidMarkAsk(bidPrice = q.bidPrice, askPrice = q.askPrice)

    LabelValueRow(label = "Last Price", value = q.lastPriceRegMarket.toTwoDecimalPlacesString())

    LabelValueRow(
        label = "Change",
        value = buildChangeDollarChangePercentDisplayString(
            q.changeDollarRegMarket, q.changePctRegMarket
        ),
        valueColor = glColorRegular
    )

    // Check if prepost data is available
    if (q.prepostChangeDollar != 0.0){
        LabelValueRow(label = "Prepost Price", value = q.prepostPrice.toTwoDecimalPlacesString())
        LabelValueRow(
            label = "Prepost Change",
            value = buildChangeDollarChangePercentDisplayString(
                q.prepostChangeDollar, q.prepostChangePct
            ),
            valueColor = glColorPrepost
        )
    }

    LabelValueRow(label = "Open Price", value = q.openPrice.toTwoDecimalPlacesString())

    LabelValueRow(label = "Previous Close", value = q.prevClose.toTwoDecimalPlacesString())

    LabelValueRow(label = "Volume", value = q.volume.toCommaString())

    LabelValueRow(label = "Average Volume", value = q.avgVolume.toCommaString())

    LabelValueRow(
        label = "Volume Difference",
        value = volDiff.toCommaString(),
        valueColor = determineColorForPosOrNegValue(volDiff.toDouble())
    )

    LabelValueRow(
        label = "Volume/Avg Ratio",
        value = getRatio(q.volume, q.avgVolume),
        valueColor = volDiffColor
    )

    Spacer(modifier = Modifier.height(20.dp))

    ValueRangeBar(
        highValue = q.daysRangeHigh,
        highLabel = "Day's High",
        curPrice = q.lastPriceRegMarket,
        lowValue = q.daysRangeLow,
        lowLabel = "Day's Low"
    )

    Spacer(modifier = Modifier.height(20.dp))

    ValueRangeBar(
        highValue = q.fiftyTwoWeekRangeHigh,
        highLabel = "52wk High",
        curPrice = q.lastPriceRegMarket,
        lowValue = q.fiftyTwoWeekRangeLow,
        lowLabel = "52wk Low"
    )

    Spacer(modifier = Modifier.height(20.dp))

    LabelValueRow(label = "Net Assets", value = "$${q.netAssetsAbbreviatedString}")

    LabelValueRow(label = "Net Asset Value", value = "$${q.nav}")

    LabelValueRow(
        label = "Beta",
        value = q.betaFiveYearMonthly.toString(),
        labelSubscript = "5yr Monthly"
    )

    LabelValueRow(label = "PE Ratio", value = q.peRatioTTM.toString(), labelSubscript = "TTM")

    LabelValueRow(label = "Yield", value = "%${q.yieldPercentage}")

    LabelValueRow(label = "YTD Return", value = "%${q.yearToDateTotalReturn}")
    
    LabelValueRow(
        label = "Expense Ratio",
        value = "%${q.expenseRatioNetPercentage}",
        labelSubscript = "Net"
    )

    LabelValueRow(label = "Inception Date", value = formatDateToMMDDYYYYString(q.inceptionDate))

}


/**
 * Displays the Dividend Yield $ and % as... $1.01 (%0.20)
 */
private fun formatDivYield(divValue: Double, divPct: Double): String{
    return if (divValue == 0.0){
        "No Dividends"
    }else{
        "${divValue.toTwoDecimalPlacesString()} (%${divPct.toTwoDecimalPlacesString()})"
    }
}