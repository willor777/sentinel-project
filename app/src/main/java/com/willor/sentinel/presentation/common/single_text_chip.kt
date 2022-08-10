package com.willor.sentinel.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.willor.sentinel.ui.theme.Sizes



@Composable
fun SingleTextChip(
    text: String,
    textSize: Int,
    fontWeight: FontWeight = FontWeight.Bold,
    textColor: Color = Color.White,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit = {}
){

    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(Sizes.HORIZONTAL_PADDING_LARGE, Sizes.VERTICAL_PADDING_NORMAL)
            .clickable { onClick() },
        shape = RoundedCornerShape(Sizes.ROUNDED_CORNER_NORMAL),
        elevation = Sizes.ELEVATION_FOR_CARD,
    ){
        Column(
            modifier = Modifier.wrapContentSize()
                .background(color)
                .padding(Sizes.HORIZONTAL_PADDING_LARGE, Sizes.VERTICAL_PADDING_NORMAL)
        ){
                Text(
                    text = text,
                    fontSize = textSize.sp,
                    color = textColor,
                    fontWeight = fontWeight
                )
        }
    }
}