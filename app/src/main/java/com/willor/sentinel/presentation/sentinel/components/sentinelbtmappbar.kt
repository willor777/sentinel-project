package com.willor.sentinel.presentation.sentinel.components

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp


@Composable
fun SentinelBottomAppBar(
    editWatchlistOnClick: () -> Unit
){

    BottomAppBar(
        backgroundColor = MaterialTheme.colorScheme.primary
    ) {

        BottomNavigationItem(
            selected = false,
            label = {
                Text("Edit Watchlist", fontSize = 9.sp, color = Color.White)
            },
            alwaysShowLabel = true,
            onClick = {
                editWatchlistOnClick()
            },
            icon = {
                Icon(Icons.Filled.EditNote, "edit watchlist", tint = Color.White)
            }
        )
    }
}