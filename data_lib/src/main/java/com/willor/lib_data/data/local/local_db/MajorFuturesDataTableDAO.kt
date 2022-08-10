package com.willor.lib_data.data.local.local_db

import androidx.room.*
import com.willor.lib_data.domain.models.MajorFuturesDataEntity
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