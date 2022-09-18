package com.willor.sentinel.presentation.quote.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.willor.sentinel.ui.theme.Sizes

@Composable
fun TickerHeader(
    txt: String
){
    Row(
        Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_PADDING_SMALL),
        horizontalArrangement = Arrangement.Start
    ){
        Text(
            txt,
            fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}
