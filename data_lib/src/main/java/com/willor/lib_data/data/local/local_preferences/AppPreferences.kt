package com.willor.lib_data.data.local.local_preferences

data class AppPreferences(
    val sentinelSettingsJson: String? = "",
    val sentinelLastScan: Long? = 0,
)

