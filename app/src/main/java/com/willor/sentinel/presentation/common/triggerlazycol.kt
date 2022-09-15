package com.willor.sentinel.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.willor.lib_data.domain.models.TriggerEntity
import com.willor.sentinel.ui.theme.Sizes


@Composable
fun TriggerLazyCol(
    listOfTriggers: List<TriggerEntity>,
    onCardClicked: (ticker: String) -> Unit = {}
){
    Column(
        modifier = Modifier
            .fillMaxHeight(.70f)
            .padding(Sizes.HORIZONTAL_EDGE_PADDING, Sizes.VERTICAL_EDGE_PADDING)
            .background(MaterialTheme.colorScheme.tertiary)
    ) {

        Header()

        Spacer(modifier = Modifier.height(Sizes.CONTENT_SPACER_NORMAL))

        TriggerLazyColContent(
            listOfTriggers = listOfTriggers,
            onCardClicked = {ticker -> onCardClicked(ticker)}
        )

    }
}


@Composable
private fun Header(){

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            "Triggers Found Today",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
        )
    }

}


@Composable
private fun TriggerLazyColContent(
    listOfTriggers: List<TriggerEntity>,
    onCardClicked: (ticker: String) -> Unit = {}
){
    LazyColumn(
        modifier = Modifier
            .padding(
                Sizes.HORIZONTAL_PADDING_NORMAL, Sizes.VERTICAL_PADDING_NORMAL
            ),
        verticalArrangement = Arrangement.spacedBy(Sizes.CONTENT_SPACER_LARGE)
    ){
        items(listOfTriggers.size){i ->
            TriggerCard(
                triggerEntity = listOfTriggers[i],
                onCardClicked = { ticker -> onCardClicked(ticker) }
            )
        }

    }
}