package com.willor.sentinel.presentation.dash.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun FailedToLoadToast(show: Boolean){

    if (show){
        Toast.makeText(
            LocalContext.current,
            "Error loading data, Please restart Application", Toast.LENGTH_LONG).show()

    }
}