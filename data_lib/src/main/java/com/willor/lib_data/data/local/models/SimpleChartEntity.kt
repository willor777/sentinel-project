package com.willor.lib_data.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.willor.ktstockdata.historical_data.charts.simplechart.SimpleStockChart
import com.willor.lib_data.utils.DbConstants


fun SimpleStockChart.toSimpleChartEntity(): SimpleChartEntity{
    return SimpleChartEntity(
        ticker = ticker,
        timeSaved = System.currentTimeMillis(),
        interval = interval,
        periodRange = periodRange,
        prepost = prepost,
        data = Gson().toJson(this)
    )
}


fun SimpleChartEntity.toSimpleStockChart(): SimpleStockChart{
    return Gson().fromJson(this.data, SimpleStockChart::class.java)
}


@Entity(tableName = DbConstants.SIMPLE_CHART_TABLE)
data class SimpleChartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo val ticker: String = "NONE",
    @ColumnInfo val timeSaved: Long = System.currentTimeMillis(),
    @ColumnInfo val interval: String? = null,
    @ColumnInfo val periodRange: String? = null,
    @ColumnInfo val prepost: Boolean? = false,
    @ColumnInfo val data: String? = null
)