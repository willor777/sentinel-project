package com.willor.sentinel.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.willor.sentinel.R


@Composable
fun NoDataImage(
    modifier: Modifier? = null
){
    val m = modifier ?: Modifier.wrapContentSize()
    val img = painterResource(id = R.raw.no_data_found)
    Image(img, "nodatafound", modifier = m)
}