package com.willor.sentinel.presentation.quote.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LabelValueRow(
    label: String,
    value: String,
    valueColor: Color? = null,
    labelSubscript: String? = null,
    labelFontSize: Int? = null
){

    Column(
        modifier = Modifier.wrapContentSize()
    ){

        // Outer row containing <Row for labelTxt + subscript> and valueTxt
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

        // Underline Effect
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Spacer(Modifier.height(1.dp).fillMaxWidth(.85f).background(Color.LightGray))

        }


    }



}
