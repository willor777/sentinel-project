package com.willor.sentinel.presentation.quote.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.willor.ktstockdata.marketdata.dataobjects.EtfQuote
import com.willor.ktstockdata.marketdata.dataobjects.StockQuote
import com.willor.sentinel.presentation.common.BidMarkAsk
import com.willor.sentinel.utils.*


@Composable
internal fun StockQuoteDisplayV2(stockQuote: State<StockQuote?>){

    val q = stockQuote.value!!
    val glColorRegular = determineColorForPosOrNegValue(q.changePctRegMarket)
    val glColorPrepost = determineColorForPosOrNegValue(q.prepostChangeDollar)
    val volDiff = q.volume - q.avgVolume
    val volDiffColor = determineColorForPosOrNegValue(volDiff.toDouble())

    // determine cur price based on afterhours / cur hours
    val curPrice = if (q.prepostChangeDollar == 0.0){q.lastPriceRegMarket} else { q.prepostPrice }


    TickerHeader(txt ="${q.ticker} Quote")

    Spacer(modifier = Modifier.height(20.dp))

    BidMarkAsk(bidPrice = q.bidPrice, askPrice = q.askPrice)

    // Underline Effect
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        Spacer(Modifier.height(1.dp).fillMaxWidth(.85f).background(Color.LightGray))

    }

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
        curValue = q.lastPriceRegMarket,
        lowValue = q.daysRangeLow,
        lowLabel = "Day's Low",
        showTopRowCurPrice = true,
        btmRowValue = q.daysRangeLow + ((q.daysRangeHigh - q.daysRangeLow) / 2.0),
        showMidPoint = true
    )

    Spacer(modifier = Modifier.height(20.dp))

    ValueRangeBar(
        highValue = q.fiftyTwoWeekRangeHigh,
        highLabel = "52wk High",
        curValue = curPrice,
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
internal fun ETFQuoteDisplayV2(etfQuote: State<EtfQuote?>){


    val q = etfQuote.value!!
    val glColorRegular = determineColorForPosOrNegValue(q.changePctRegMarket)
    val glColorPrepost = determineColorForPosOrNegValue(q.prepostChangeDollar)
    val volDiff = q.volume - q.avgVolume
    val volDiffColor = determineColorForPosOrNegValue(volDiff.toDouble())


    TickerHeader(txt = "${q.ticker} ")

    Spacer(modifier = Modifier.height(20.dp))

    BidMarkAsk(bidPrice = q.bidPrice, askPrice = q.askPrice)

    // Underline Effect
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        Spacer(Modifier.height(1.dp).fillMaxWidth(.85f).background(Color.LightGray))

    }
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
        curValue = q.lastPriceRegMarket,
        lowValue = q.daysRangeLow,
        lowLabel = "Day's Low"
    )

    Spacer(modifier = Modifier.height(20.dp))

    ValueRangeBar(
        highValue = q.fiftyTwoWeekRangeHigh,
        highLabel = "52wk High",
        curValue = q.lastPriceRegMarket,
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
