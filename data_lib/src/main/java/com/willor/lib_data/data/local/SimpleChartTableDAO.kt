package com.willor.lib_data.data.local

import androidx.room.*
import com.willor.lib_data.data.local.models.AdvChartEntity
import com.willor.lib_data.data.local.models.SimpleChartEntity
import com.willor.lib_data.utils.DbConstants

@Dao
interface SimpleChartTableDAO {

    @Query("SELECT * FROM ${DbConstants.SIMPLE_CHART_TABLE}")
    fun getAllFromTable(): List<SimpleChartEntity>?

    @Query("SELECT * FROM ${DbConstants.SIMPLE_CHART_TABLE} WHERE ticker = :ticker")
    fun getAllFromTableByTicker(ticker: String): List<SimpleChartEntity>?

    @Query("SELECT * FROM ${DbConstants.SIMPLE_CHART_TABLE} WHERE" +
            " ticker = :ticker AND interval = :interval " +
            "AND periodRange = :periodRange AND prepost = :prepost")
    fun getAllMatchingFromTable(
        ticker: String, interval: String,
        periodRange: String, prepost: Boolean): List<SimpleChartEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIntoTable(vararg c: SimpleChartEntity)

    @Delete
    fun deleteFromTable(c: SimpleChartEntity)

    @Query("DELETE FROM ${DbConstants.SIMPLE_CHART_TABLE}")
    fun clearTable()
}