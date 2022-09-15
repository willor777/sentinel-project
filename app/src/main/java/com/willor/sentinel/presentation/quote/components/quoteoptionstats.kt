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
import com.willor.ktstockdata.marketdata.dataobjects.OptionStats
import com.willor.sentinel.presentation.common.AnimatedLoadingWidget
import com.willor.sentinel.presentation.common.NoDataImage
import com.willor.sentinel.ui.theme.Sizes
import com.willor.sentinel.utils.*


@Composable
fun OptionStatsDisplay(
    optionStats: State<OptionStats?>,
    optionStatsFailed: State<Boolean>,
    onRetryClicked: () -> Unit,
){

    val showContent = remember{ mutableStateOf(false) }

    if (optionStats.value == null){showContent.value = false}

    // Loading Screen if no content is available + no fail flag
    if (!showContent.value && !optionStatsFailed.value){
        Column(
            Modifier
                .fillMaxHeight(.6f)
                .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_EDGE_PADDING)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
        ){

            // Display loading screen until either stock or etf quote loads
            AnimatedLoadingWidget(
                conditionCheck = { optionStats.value != null},
                onConditionSuccess = { showContent.value = true },
                maxTime = 20_000,
                onMaxTimeReached = {
                }
            ){
                Text(
                    "Option Stats Loading..."
                )
            }
        }
    }

    // Failed to load
    else if (optionStatsFailed.value){

        Column(
            Modifier
                .fillMaxHeight(.6f)
                .verticalScroll(rememberScrollState())
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
        Column(
            Modifier
                .fillMaxHeight(.6f)
                .verticalScroll(rememberScrollState())
                .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_EDGE_PADDING)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
        ) {

            // Header row
            QuoteDataHeaderRow("Option Stats")

            OptionStatsDisplayContent(optionStats = optionStats)
        }
    }



}


// TODO Make the IvHigh ttm, IvLow ttm , curIv into a bar gauge
@Composable
private fun OptionStatsDisplayContent(
    optionStats: State<OptionStats?>
){
    val q = optionStats.value!!

    val ivChangeColor = determineColorForPosOrNegValue(q.impVolChangeToday)

    val optionVolumeDiff = q.optionVolumeToday - q.optionVolumeAvgThirtyDay
    val optionVolRatio = getRatio(q.optionVolumeToday, q.optionVolumeAvgThirtyDay)
    val optionVolumeDiffColor = determineColorForPosOrNegValue(optionVolumeDiff.toDouble())

    val optionOiDiff = q.openInterestToday - q.openInterestThirtyDay
    val optionOiRatio = getRatio(q.openInterestToday, q.openInterestThirtyDay)
    val optionOiDiffColor = determineColorForPosOrNegValue(optionOiDiff.toDouble())

    val pcvColor = determineColorForPutCallRatio(value = q.putCallVolumeRatio)
    val pcoiColor = determineColorForPutCallRatio(value = q.putCallOpenInterestRatio)



    TickerHeader(ticker = q.ticker)

    Spacer(modifier = Modifier.height(20.dp))

    LabelValueRow(
        label = "Put Call Volume Ratio",
        value = q.putCallVolumeRatio.toString(),
        valueColor = pcvColor
    )

    LabelValueRow(
        label = "Option Volume",
        value = q.optionVolumeToday.toCommaString(),
        labelSubscript = "Today"
    )

    LabelValueRow(
        label = "Option Volume",
        value = q.optionVolumeAvgThirtyDay.toCommaString(),
        labelSubscript = "30d Avg"
    )

    LabelValueRow(
        label = "Option Volume",
        value = optionVolumeDiff.toCommaString(),
        valueColor = optionVolumeDiffColor,
        labelSubscript = "Difference"
    )

    LabelValueRow(
        label = "Option Volume",
        value = optionVolRatio,
        valueColor = optionVolumeDiffColor,
        labelSubscript = "Difference Ratio"
    )

    LabelValueRow(
        label = "Put Call OI Ratio",
        value = q.putCallOpenInterestRatio.toString(),
        valueColor = pcoiColor
    )

    LabelValueRow(
        label = "OI",
        value = q.openInterestToday.toCommaString(),
        labelSubscript = "Today"
    )

    LabelValueRow(
        label = "OI",
        value = q.openInterestThirtyDay.toCommaString(),
        labelSubscript = "30d Avg"
    )

    LabelValueRow(
        label = "OI",
        value = optionOiDiff.toCommaString(),
        valueColor = optionOiDiffColor,
        labelSubscript = "Difference"
    )

    LabelValueRow(
        label = "OI",
        value = optionOiRatio,
        valueColor = optionOiDiffColor,
        labelSubscript = "Difference Ratio"
    )

    LabelValueRow(label = "IV", value = "%${q.impVol}")

    LabelValueRow(
        label = "IV Change Today",
        value = "%${q.impVolChangeToday}",
        valueColor = ivChangeColor
    )

    LabelValueRow(label = "Historic Volatility", value ="%${q.historicalVolatilityPercentage}" )


    LabelValueRow(label = "IV Percentile", value = "%${q.ivPercentile}")
    
    LabelValueRow(label = "IV Rank", value = "%${q.ivRank}")
    
//    LabelValueRow(label = "IV High", value = "%${q.ivHighLastYear}", labelSubscript = "TTM")

    LabelValueRow(
        label = "IV High Date",
        value = formatDateToMMDDYYYYString(q.ivHighDate),
        labelSubscript = "TTM"
    )

//    LabelValueRow(label = "IV Low", value = "%${q.ivLowLastYear}", labelSubscript = "TTM")

    LabelValueRow(
        label = "IV Low Date",
        value = formatDateToMMDDYYYYString(q.ivLowDate),
        labelSubscript = "TTM"
    )



    ValueRangeBar(
        highValue = q.ivHighLastYear,
        highLabel = "IV High TTM",
        curPrice = q.impVol,
        lowValue = q.ivLowLastYear,
        lowLabel = "IV Low TTM",
        topRowValue = q.impVol,
        btmRowValue = q.ivLowLastYear + ((q.ivLowLastYear + q.ivHighLastYear) / 2.0),
        showMidPoint = true,
        valueType = "%"
    )
}
