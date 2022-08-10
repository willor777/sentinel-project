package com.willor.lib_data.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.willor.ktstockdata.watchlists_data.dataobjects.Watchlist
import com.willor.lib_data.utils.DbConstants


fun Watchlist.toWatchlistEntity(): WatchlistEntity{
    return WatchlistEntity(
        name,
        System.currentTimeMillis(),
        Gson().toJson(this)
    )
}


fun WatchlistEntity.toWatchlist(): Watchlist{
    return Gson().fromJson(this.data, Watchlist::class.java)
}


@Entity(tableName = DbConstants.WATCHLIST_TABLE)
data class WatchlistEntity(
    @PrimaryKey val name: String = "NONE",
    @ColumnInfo val timeSaved: Long = System.currentTimeMillis(),
    @ColumnInfo val data: String = "",
)