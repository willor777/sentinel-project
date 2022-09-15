package com.willor.sentinel.presentation.watchlist_setup.components

import androidx.lifecycle.ViewModel
import com.willor.lib_data.data.local.preferences.DatastorePrefsManager
import javax.inject.Inject

class WatchlistSetupViewModel @Inject constructor(
    private val prefsManager: DatastorePrefsManager
): ViewModel() {
}