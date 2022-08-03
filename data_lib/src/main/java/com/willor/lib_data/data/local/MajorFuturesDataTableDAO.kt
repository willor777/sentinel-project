package com.willor.lib_data.data.local

import androidx.room.*
import com.willor.lib_data.data.local.models.ETFQuoteEntity
import com.willor.lib_data.data.local.models.MajorFuturesDataEntity
import com.willor.lib_data.utils.DbConstants


@Dao
interface MajorFuturesDataTableDAO {

    @Query("SELECT * FROM ${DbConstants.FUTURES_DATA_TABLE}")
    fun getAllFromTable(): List<MajorFuturesDataEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIntoTable(vararg q: MajorFuturesDataEntity)

    @Delete
    fun deleteFromTable(q: MajorFuturesDataEntity)

    @Query("DELETE FROM ${DbConstants.FUTURES_DATA_TABLE}")
    fun clearTable()
}