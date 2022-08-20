package com.willor.lib_data.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.willor.ktstockdata.quote_data.dataobjects.OptionStats
import com.willor.lib_data.utils.DbConstants
import java.util.*


fun OptionStats.toOptionStatsEntity(): OptionStatsEntity{
    return OptionStatsEntity(
        ticker, System.currentTimeMillis(), impVol, impVolChangeToday,
        historicalVolatilityPercentage, ivPercentile, ivRank, ivHighLastYear,
        ivHighDate, ivLowLastYear, ivLowDate, putCallVolumeRatio, optionVolumeToday,
        optionVolumeAvgThirtyDay, putCallOpenInterestRatio, openInterestToday, 
        openInterestThirtyDay
    )
}


fun OptionStatsEntity.toOptionStats(): OptionStats{
    return OptionStats(
        ticker, ivPercentage!!, ivPercentageTodaysChange!!, historicalVolatilityPercentage!!, 
        ivPercentile!!, ivRank!!, ivHighLastYear!!, ivHighDate!!, ivLowLastYear!!, ivLowDate!!, 
        putCallVolumeRatio!!, optionVolumeToday!!, optionVolumeAvgThirtyDay!!, putCallOpenInterestRatio!!,
        openInterestToday!!, openInterestThirtyDay!!,
    )
}


@Entity(tableName = DbConstants.OPTION_STATS_TABLE)
data class OptionStatsEntity(
    @PrimaryKey val ticker: String = "NONE",
    @ColumnInfo val timeSaved: Long = System.currentTimeMillis(),
    @ColumnInfo val ivPercentage: Double? = null,
    @ColumnInfo val ivPercentageTodaysChange: Double? = null,
    @ColumnInfo val historicalVolatilityPercentage: Double? = null,
    @ColumnInfo val ivPercentile: Double? = null,
    @ColumnInfo val ivRank: Double? = null,
    @ColumnInfo val ivHighLastYear: Double? = null,
    @ColumnInfo val ivHighDate: Date? = null,
    @ColumnInfo val ivLowLastYear: Double? = null,
    @ColumnInfo val ivLowDate: Date? = null,
    @ColumnInfo val putCallVolumeRatio: Double? = null,
    @ColumnInfo val optionVolumeToday: Int? = null,
    @ColumnInfo val optionVolumeAvgThirtyDay: Int? = null,
    @ColumnInfo val putCallOpenInterestRatio: Double? = null,
    @ColumnInfo val openInterestToday: Int? = null,
    @ColumnInfo val openInterestThirtyDay: Int? = null,
)