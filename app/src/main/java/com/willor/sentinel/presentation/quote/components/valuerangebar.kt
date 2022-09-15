package com.willor.sentinel.presentation.quote.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.willor.sentinel.ui.theme.MyColors
import com.willor.sentinel.utils.toTwoDecimalPlaceString
import com.willor.sentinel.utils.toTwoDecimalPlacesString


@Composable
fun ValueRangeBar(
    highValue: Double,
    highLabel: String,
    curPrice: Double,
    lowValue: Double,
    lowLabel: String,
    valueType: String = "$",
    topRowValue: Double? = null,
    btmRowValue: Double? = null,
    showMidPoint: Boolean = false,
){

    // Total size of range and cur progress
    val totalRangeSize = highValue - lowValue
    val curProgress = curPrice - lowValue

    // % of bar that should be filled
    val pctComplete = curProgress / totalRangeSize

    // State of the bar
    val sliderPos = remember{ pctComplete.toFloat() }

    // Root layout
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()){

        // current value
        if (topRowValue != null){
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                Text(
                    text = "Current: ",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Text(
                    text = "$valueType${topRowValue.toTwoDecimalPlaceString()}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }

        // Base bar with Left & Right end caps
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom

        ){

            // Days low end cap
            HighLowEndCaps(
                label = lowLabel,
                value = lowValue.toTwoDecimalPlacesString(),
            )

            LinearProgressIndicator(
                progress = sliderPos,
                color = MyColors.highLowBarGreen,
                backgroundColor = MyColors.highLowBarRed,
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .height(15.dp)
            )

            // Days high end cap
            HighLowEndCaps(
                label = highLabel,
                value = highValue.toTwoDecimalPlacesString(),
            )
        }

        // ^ Symbol denoting the mid point on the bottom of bar
        if (showMidPoint){
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ){
                Text(
                    text = "|",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }

        // Mid Point $<value>
        if (btmRowValue != null){
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ){
                Text(
                    text = "Mid Point: ",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Text(
                    text = "$valueType${btmRowValue.toTwoDecimalPlaceString()}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
    }







}

@Composable
fun HighLowEndCaps(label: String, value: String){
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Bottom
//                .height(maxHeight)
        ){
            Text(
                label,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Text(
                value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }

}



