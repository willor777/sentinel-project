package com.willor.sentinel.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.willor.sentinel.ui.theme.Sizes

@Composable
fun CurrentSettingsLabelValueRow(
    label: String,
    value: String
){
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_PADDING_NORMAL),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ){

        // Label Text
        Text(
            label,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
            fontFamily = MaterialTheme.typography.bodySmall.fontFamily
        )

        // Value text
        Text(
            value,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
            fontFamily = MaterialTheme.typography.bodySmall.fontFamily
        )
    }
}