package com.willor.sentinel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ramcosta.composedestinations.DestinationsNavHost
import com.willor.lib_data.SymbolDataHelper
import com.willor.lib_data.utils.printToTestingLog
import com.willor.sentinel.presentation.NavGraphs
import com.willor.sentinel.ui.theme.SentinelTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val locTAG = MainActivity::class.java.name

    private lateinit var datastore: DataStore<Preferences>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO Delete Me!!
        printToTestingLog(
            SymbolDataHelper.loadsyms()
        )

        setContent {
            SentinelTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Create a Composable, set with @Destination(start = true)
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }
}
