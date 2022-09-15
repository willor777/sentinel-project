package com.willor.sentinel.presentation.quote.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp


@Composable
fun LabelValueRow(
    label: String,
    value: String,
    valueColor: Color? = null,
    labelSubscript: String? = null,
    labelFontSize: Int? = null
){

    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ){

        // Label needs own row so subscript will be grouped & Top Aligned
        Row(
            modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.Top
        ){

            Text(
                text = label,
                fontSize = labelFontSize?.sp ?: MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
                fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
                color = MaterialTheme.colorScheme.onTertiary
            )

            // Check if it has subscript
            if (labelSubscript != null){
                Text(
                    text = labelSubscript,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    color = MaterialTheme.colorScheme.onTertiary,
                )
            }
        }

        // Value
        Text(
            text = value,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
            color = valueColor ?: MaterialTheme.colorScheme.onTertiary
        )
    }
}