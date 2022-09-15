package com.willor.sentinel.presentation.quote.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun QuoteDataHeaderRow(text: String){
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ){
        Text(
            text,
            fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontStyle = MaterialTheme.typography.titleLarge.fontStyle
        )
    }
}