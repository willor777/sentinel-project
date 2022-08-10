package com.willor.sentinel.presentation.dash.components

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.willor.lib_data.utils.printToDEBUGTEMP
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun DashBottomAppBar(){

    val sentinelStartSelected = remember{ mutableStateOf(false)}
    var editWatchlistSelected = remember{ mutableStateOf(false)}

    BottomAppBar(
        backgroundColor = MaterialTheme.colorScheme.primary,
    ) {

        // Edit Sentinel Watchlist
        BottomNavigationItem(
            selected = editWatchlistSelected.value,
            label = {
                Text("Edit Watchlist", fontSize = 9.sp, color = Color.White)
            },
            alwaysShowLabel = true,
            onClick = {
                editWatchlistSelected.value = true
                printToDEBUGTEMP("Edit watchlist clicked")
            },
            icon = {
                Icon(Icons.Filled.EditNote, "edit watchlist", tint = Color.White)
            }
        )

        // Sentinel Start Selected
        BottomNavigationItem(
            selected = sentinelStartSelected.value,
            label = {
                    Text("Start Sentinel", fontSize = 9.sp, color = Color.White)
            },
            alwaysShowLabel = true,
            onClick = {
                      printToDEBUGTEMP("Start Sentinel Clicked")
            },
            icon = {
                Icon(Icons.Filled.Radar, "start sentinel", tint = Color.White)
            }
        )


    }
}