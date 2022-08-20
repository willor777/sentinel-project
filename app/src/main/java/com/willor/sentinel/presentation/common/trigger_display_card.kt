package com.willor.sentinel.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.willor.lib_data.domain.models.TriggerEntity
import com.willor.sentinel.ui.theme.Sizes
import com.willor.sentinel.utils.buildChangeDollarChangePercentDisplayString
import com.willor.sentinel.utils.determineColorForPosOrNegValue
import com.willor.sentinel.utils.toDateString
import com.willor.sentinel.utils.toUSDollarString


@Composable
fun TriggerDisplayCard(
    triggerEntity: TriggerEntity,
    onCardClicked: (ticker: String) -> Unit,
){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onCardClicked(triggerEntity.ticker) },
        elevation = Sizes.ELEVATION_FOR_CARD,
        shape = RoundedCornerShape(Sizes.ROUNDED_CORNER_NORMAL)
    ){

        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary)

        ){

            TopRowData(triggerEntity = triggerEntity)

            Spacer(Modifier.height(Sizes.CONTENT_SPACER_NORMAL))

            CurrentPriceOfAsset(triggerEntity = triggerEntity)

            TriggerTime(triggerEntity = triggerEntity)

            TriggerType(triggerEntity = triggerEntity)

            TriggerRating(triggerEntity = triggerEntity)

            TriggerStrategyName(triggerEntity = triggerEntity)

            Spacer(Modifier.height(Sizes.CONTENT_SPACER_NORMAL))

        }
    }
}


@Composable
fun TopRowData(
    triggerEntity: TriggerEntity
){


    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ){

        Text(
            text = triggerEntity.ticker,
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            fontWeight = MaterialTheme.typography.headlineLarge.fontWeight,
            fontFamily = MaterialTheme.typography.headlineLarge.fontFamily,
            color = MaterialTheme.colorScheme.onPrimary
        )

    }
}


@Composable
fun LabelValueRow(
    label: String,
    value: String,
    valueColor: Color? = null
){
    Row(
        Modifier.fillMaxWidth()
            .padding(
                Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_EDGE_PADDING
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

    ){

        val c = if (valueColor == null){
            MaterialTheme.colorScheme.onPrimary
        }else{
            valueColor
        }

        // Column Label
        Text(
            text = label,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
            color = MaterialTheme.colorScheme.onPrimary
        )

        // Value Label
        Text(
            text = value,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
            color = c
        )
    }
}


@Composable
fun TriggerType(
    triggerEntity: TriggerEntity
){
    val color = determineColorForPosOrNegValue(triggerEntity.triggerValue.toDouble())

    val triggerValue = if (triggerEntity.triggerValue == 1){
        "Long"
    }else{
        "Short"
    }

    LabelValueRow(label = "Trigger Type: ", value = triggerValue, valueColor = color)
}


@Composable
fun CurrentPriceOfAsset(
    triggerEntity: TriggerEntity
){
    LabelValueRow(
        label = "Last Price:",
        value = "$ ${triggerEntity.priceOfAsset.toUSDollarString()}"
    )
}


@Composable
fun TriggerRating(
    triggerEntity: TriggerEntity
){

    LabelValueRow(
        label = "Trigger Rating:",
        value = triggerEntity.rating.toString()
    )

}


@Composable
fun TriggerTime(
    triggerEntity: TriggerEntity
){

    LabelValueRow(
        label = "Time Found:",
        value = triggerEntity.timestamp.toDateString()
    )

}


@Composable
fun TriggerStrategyName(
    triggerEntity: TriggerEntity
){

    LabelValueRow(
        label = "Scanner Name:",
        value = triggerEntity.strategyName
    )

}


