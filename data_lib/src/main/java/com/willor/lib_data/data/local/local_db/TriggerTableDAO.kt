package com.willor.lib_data.data.local.local_db

import androidx.room.*
import com.willor.lib_data.domain.models.TriggerEntity
import com.willor.lib_data.domain.models.WatchlistEntity
import com.willor.lib_data.utils.DbConstants
import kotlinx.coroutines.flow.Flow


@Dao
interface TriggerTableDAO {

    @Query("SELECT * FROM ${DbConstants.TRIGGER_TABLE} LIMIT :limit")
    fun getAllFromTable(limit: Int = 10): List<TriggerEntity>?


    @Query("SELECT * FROM ${DbConstants.TRIGGER_TABLE} LIMIT :limit")
    fun getAllFromTableAsFlow(limit: Int = 10): Flow<List<TriggerEntity>?>


    @Query("SELECT * FROM ${DbConstants.TRIGGER_TABLE} WHERE :startTime < timestamp < :endTime" +
            " LIMIT :limit")
    fun getAllFromTableInTimeRange(startTime: Long, endTime: Long, limit: Int = 10
    ): List<TriggerEntity>?


    @Query("SELECT * FROM ${DbConstants.TRIGGER_TABLE} WHERE :startTime < timestamp AND timestamp < :endTime" +
            " Limit :limit")
    fun getAllFromTableInTimeRangeAsFlow(startTime: Long, endTime: Long, limit: Int = 10
    ): Flow<List<TriggerEntity>?>


    @Query("SELECT * FROM ${DbConstants.TRIGGER_TABLE} WHERE ticker = :ticker")
    fun getAllTriggersForTicker(ticker: String): List<TriggerEntity>?


    @Query("SELECT * FROM ${DbConstants.TRIGGER_TABLE} WHERE ticker = :ticker")
    fun getAllTriggersForTickerAsFlow(ticker: String): Flow<List<TriggerEntity>?>


    @Query("SELECT * FROM ${DbConstants.TRIGGER_TABLE} WHERE ticker = :ticker AND" +
            " :startTime < timestamp < :endTime")
    fun getAllTriggersForTickerInTimeRange(
        ticker: String, startTime: Long, endTime: Long
    ): List<TriggerEntity>?


    @Query("SELECT * FROM ${DbConstants.TRIGGER_TABLE} WHERE ticker = :ticker AND" +
            " :startTime < timestamp < :endTime")
    fun getAllTriggersForTickerInTimeRangeAsFlow(
        ticker: String, startTime: Long, endTime: Long
    ): Flow<List<TriggerEntity>?>


    // TODO
//    fun getAllLongTriggersForTicker(ticker: String)
//    fun getAllShortTriggersForTicker(ticker: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIntoTable(vararg t: TriggerEntity)


    @Delete
    fun deleteFromTable(t: TriggerEntity)


    @Query("DELETE FROM ${DbConstants.TRIGGER_TABLE}")
    fun clearTable()
    
    
}