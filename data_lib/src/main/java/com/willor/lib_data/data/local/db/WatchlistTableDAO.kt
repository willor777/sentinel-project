package com.willor.lib_data.data.local.db

import androidx.room.*
import com.willor.lib_data.domain.models.WatchlistEntity
import com.willor.lib_data.utils.DbConstants


@Dao
interface WatchlistTableDAO {
    @Query("SELECT * FROM ${DbConstants.WATCHLIST_TABLE}")
    fun getAllFromTable(): List<WatchlistEntity>?

    @Query("SELECT * FROM ${DbConstants.WATCHLIST_TABLE} WHERE name = :name LIMIT 1")
    fun getWatchlistByName(name: String): WatchlistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIntoTable(vararg c: WatchlistEntity)

    @Delete
    fun deleteFromTable(c: WatchlistEntity)

    @Query("DELETE FROM ${DbConstants.WATCHLIST_TABLE}")
    fun clearTable()
}