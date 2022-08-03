package com.willor.lib_data.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.willor.ktstockdata.historical_data.charts.advancedchart.AdvancedStockChart
import com.willor.lib_data.utils.DbConstants


fun AdvChartEntity.toAdvancedStockChart(): AdvancedStockChart{
    return Gson().fromJson(data!!, AdvancedStockChart::class.java)
}

fun AdvancedStockChart.toAdvChartEntity(): AdvChartEntity{
    return AdvChartEntity(
        ticker = ticker,
        interval = interval,
        periodRange = periodRange,
        prepost = prepost,
        data = Gson().toJson(this)
    )
}


@Entity(tableName = DbConstants.ADV_CHART_TABLE)
data class AdvChartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo val ticker: String = "NONE",
    @ColumnInfo val timeSaved: Long = System.currentTimeMillis(),
    @ColumnInfo val interval: String? = null,
    @ColumnInfo val periodRange: String? = null,
    @ColumnInfo val prepost: Boolean? = false,
    @ColumnInfo val data: String? = null
)