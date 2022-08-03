package com.willor.lib_data.data.local

import androidx.room.*
import com.willor.ktstockdata.historical_data.charts.advancedchart.AdvancedStockChart
import com.willor.lib_data.data.local.models.AdvChartEntity
import com.willor.lib_data.utils.DbConstants


@Dao
interface AdvChartTableDAO {

    @Query("SELECT * FROM ${DbConstants.ADV_CHART_TABLE}")
    fun getAllFromTable(): List<AdvChartEntity>?


    @Query("SELECT * FROM ${DbConstants.ADV_CHART_TABLE} WHERE ticker = :ticker")
    fun getAllFromTableByTicker(ticker: String): List<AdvChartEntity>?


    @Query("SELECT * FROM ${DbConstants.ADV_CHART_TABLE} WHERE" +
            " ticker = :ticker AND interval = :interval " +
            "AND periodRange = :periodRange AND prepost = :prepost")
    fun getAllMatchingFromTable(
        ticker: String, interval: String,
        periodRange: String, prepost: Boolean): List<AdvChartEntity>?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIntoTable(vararg c: AdvChartEntity)


    @Delete
    fun deleteFromTable(c: AdvChartEntity)


    @Query("DELETE FROM ${DbConstants.ADV_CHART_TABLE}")
    fun clearTable()
}