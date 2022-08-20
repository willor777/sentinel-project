package com.willor.sentinel.presentation.sentinel.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.willor.sentinel.ui.theme.SentinelTheme
import com.willor.sentinel.ui.theme.Sizes


@Composable
fun SentinelActiveTopAppBar(
    homeButtonOnClick: () -> Unit = {},
    settingsButtonOnClick: () -> Unit = {}
){
    SentinelTheme {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
                .height(Sizes.TOP_APP_BAR_HEIGHT)
                .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_EDGE_PADDING),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Back to home button
            IconButton(
                onClick = {
                    homeButtonOnClick()
                }
            ){
                Icon(Icons.Filled.Home, "go home", tint = Color.White)
            }

            // App bar text
            Text(
                "Sentinel",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                color = Color.White
            )

            // Settings navigation
            Button(
                onClick = {
                    Log.d("INFO", "Sentinel Top App bar nav Icon clicked")
                    settingsButtonOnClick()
                }
            ){
                Icon(Icons.Filled.Settings, "settings", tint = Color.White)
            }
        }
    }
}