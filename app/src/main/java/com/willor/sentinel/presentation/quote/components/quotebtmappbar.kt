package com.willor.sentinel.presentation.quote.components

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp


@Composable
fun QuoteBottomAppBar(
    addToWatchlistOnClick: () -> Unit = {},
){
    BottomAppBar(
        backgroundColor = MaterialTheme.colorScheme.primary,
    ) {

        // Edit Sentinel Watchlist
        BottomNavigationItem(
            selected = false,
            label = {
                Text("Add To Watchlist", fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onPrimary)
            },
            alwaysShowLabel = true,
            onClick = {
                addToWatchlistOnClick()
            },
            icon = {
                Icon(Icons.Filled.AddCircle,
                    "add too watchlist",
                    tint = MaterialTheme.colorScheme.onPrimary)
            }
        )

    }
}


