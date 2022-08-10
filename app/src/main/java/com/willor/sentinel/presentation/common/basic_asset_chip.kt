package com.willor.sentinel.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.willor.sentinel.ui.theme.Sizes
import com.willor.sentinel.ui.theme.fontFamily
import com.willor.sentinel.utils.determineColorForPosOrNegValue
import com.willor.sentinel.utils.toUSDollarString


/**
 * Clickable Card with Rounded Corner Shape that displays...
 *
 * - assetName
 * - lastPrice
 * - $ dollarChange (% pctChange)
 *      - dollarChange and pctChange are colored either green / red depending on + or - value
 */
@Composable
fun BasicAssetDisplayChip(
    assetName: String,
    lastPrice: Double,
    dollarChange: Double,
    pctChange: Double,
    widthDp: Int = 175,
    heightDp: Int = 75,
    onClick: () -> Unit = {  }
){

    val gainlossColor = determineColorForPosOrNegValue(pctChange)

    Card(
        modifier = Modifier
            .width(widthDp.dp)
            .height(heightDp.dp),
        shape = RoundedCornerShape(Sizes.ROUNDED_CORNER_NORMAL),
        elevation = Sizes.ELEVATION_FOR_CARD
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary)
                .clickable { onClick() }
        ){
            // Asset  Name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,

            ){
                Text(
                    assetName,
                    color = Color.White,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
                    fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
                    fontWeight = MaterialTheme.typography.headlineMedium.fontWeight
                )
            }

            // Asset Last Price
            Row(
                modifier = Modifier.fillMaxWidth().padding(3.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    "$${lastPrice.toUSDollarString()}",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    color = Color.White
                )
            }

            // Asset change
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    "$${dollarChange.toUSDollarString()} (% ${pctChange.toUSDollarString()})",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    color = gainlossColor
                )
            }
        }
    }
}