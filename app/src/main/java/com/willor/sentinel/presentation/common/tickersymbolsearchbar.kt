package com.willor.sentinel.presentation.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp


// As txt is typed in, a lazy column below the text edit should pop up
// -- Either Animate the rest of the UI moving down
// -- Or the Lazy Col should just show on top of the rest of the UI
// -- The lazy col should be defined height, full width. It doesn't need to be HUGE
// -- -- just 200 - 400 dp maybe
// The lazy column should display tickers matching what the user has typed in SO FAR
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TickerSymbolSearchBar(
    onTextChange: (text: String) -> Unit,
    onSearchClick: (text: String) -> Unit,
    onSuggestionItemClicked: (text: String) -> Unit,
    searchResults: State<List<List<String>>>
){
    val currentText = remember{ mutableStateOf("")}

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Column contains search bar + search results when they are available
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ){

        // Row contains SearchBar + SearchIcon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) {

            // SearchBar
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth(.9f)
                    .height(30.dp)
                    .background(Color.LightGray),
                value = currentText.value,
                onValueChange = {userTxt ->

                    var capped = ""

                    // Verify + Capitalize txt
                    for (c in userTxt){
                        if (c.isLetter()){
                            capped += c.uppercase()
                        }
                    }

                    currentText.value = capped
                    onTextChange(currentText.value)
                },
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
                    fontFamily = MaterialTheme.typography.headlineMedium.fontFamily
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClick(currentText.value)
                        currentText.value = ""
                        focusManager.clearFocus()
                    }
                )
            )

            // SearchIcon
            Icon(
                Icons.Filled.Search,
                "search",
                modifier = Modifier.background(Color.DarkGray)
                    .height(30.dp)
                    .clickable {
                       onSearchClick(currentText.value)
                        currentText.value = ""
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    },
                tint = Color.White
            )
        }

        // Show search results (triggering animation)
        if (searchResults.value.isNotEmpty()){
            SearchResults(searchResultsList = searchResults.value,
            onSuggestionItemClicked = {
                onSuggestionItemClicked(it)
                currentText.value = ""
                focusManager.clearFocus()
            })
        }
    }


}


@Composable
private fun SearchResults(
    searchResultsList: List<List<String>>,
    onSuggestionItemClicked: (text: String) -> Unit
){

    // The lazy col will get no bigger than 120.dp, and if smaller, will be just big enough to
    // show content
    val m = if (searchResultsList.size > 3){
        Modifier.fillMaxWidth().height(120.dp)
    }else{
        Modifier.wrapContentHeight().fillMaxWidth()
    }

    Column(modifier = m){
        LazyColumn{
            items(searchResultsList.size){i ->
                SearchResultItem(searchResultsList[i],
                onSuggestionItemClicked = {
                    onSuggestionItemClicked(it)
                })
            }
        }
    }
}


@Composable
private fun SearchResultItem(
    stockInfo: List<String>,
    onSuggestionItemClicked: (text: String) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth().height(20.dp)
            .clickable {
                       onSuggestionItemClicked(stockInfo[0])
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = stockInfo[0]
        )
        Text(
            text = stockInfo[1].trimEnd(),
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        )
    }
}