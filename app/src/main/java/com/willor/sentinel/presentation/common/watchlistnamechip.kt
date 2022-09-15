package com.willor.sentinel.presentation.dash.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.willor.ktstockdata.watchlistsdata.WatchlistOptions
import com.willor.sentinel.presentation.common.SingleTextChip
import com.willor.sentinel.ui.theme.Sizes


/**
 * Row of Chips that allow user to choose what watchlist is to be displayed on Dashboard.
 */
@Composable
fun WatchlistNameChip(
    watchlistOptions: List<WatchlistOptions>,
    onClick: (WatchlistOptions) -> Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_PADDING_NORMAL)
            .background(MaterialTheme.colorScheme.tertiary)
    ){

        LazyRow(Modifier.fillMaxWidth()){
            items(watchlistOptions.size){index ->

                SingleTextChip(
                    text = formatWatchlistName(watchlistOptions[index].name),
                    textSize = MaterialTheme.typography.titleMedium.fontSize.value.toInt(),
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight!!,
                ){
                    onClick(watchlistOptions[index])
                }
            }
        }

    }
}


private fun formatWatchlistName(wlName: String): String{
    val split = wlName.split("_")
    val fixed = mutableListOf<String>()

    split.map {
        fixed.add(
            it.lowercase().replaceFirstChar { firstChar ->
                firstChar.uppercase()
            }
        )
    }

    return fixed.joinToString(" ")
}