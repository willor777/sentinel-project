package com.willor.lib_data.data.local.db

import androidx.room.*
import com.willor.lib_data.domain.models.OptionStatsEntity
import com.willor.lib_data.utils.DbConstants


@Dao
interface OptionStatsTableDAO {
    @Query("SELECT * FROM ${DbConstants.OPTION_STATS_TABLE}")
    fun getAllFromTable(): List<OptionStatsEntity>?

    @Query("SELECT * FROM ${DbConstants.OPTION_STATS_TABLE} WHERE ticker = :ticker LIMIT 1")
    fun getByTicker(ticker: String): OptionStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIntoTable(vararg q: OptionStatsEntity)

    @Delete
    fun deleteFromTable(q: OptionStatsEntity)

    @Query("DELETE FROM ${DbConstants.OPTION_STATS_TABLE}")
    fun clearTable()
}