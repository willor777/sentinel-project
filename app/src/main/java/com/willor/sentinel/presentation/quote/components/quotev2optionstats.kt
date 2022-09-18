package com.willor.sentinel.presentation.quote.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.willor.ktstockdata.marketdata.dataobjects.OptionStats
import com.willor.sentinel.utils.*

@Composable
internal fun OptionStatsDisplayContentV2(
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



    TickerHeader(txt = "Option Statistics")

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
        curValue = q.impVol,
        lowValue = q.ivLowLastYear,
        lowLabel = "IV Low TTM",
        showTopRowCurPrice = true,
        btmRowValue = q.ivLowLastYear + ((q.ivLowLastYear + q.ivHighLastYear) / 2.0),
        showMidPoint = true,
        valueType = "%"
    )
}
