package com.willor.lib_data.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.willor.ktstockdata.misc_data.dataobjects.SnRLevels
import com.willor.lib_data.utils.DbConstants


fun SnRLevels.toSnRLevelsEntity():SnRLevelsEntity{
    return SnRLevelsEntity(
        ticker, System.currentTimeMillis(), r3, r2, r1, approxPrice, s1, s2, s3, fiftyTwoWeekHigh,
        fibonacci62Pct, fibonacci50Pct, fibonacci38Pct, fiftyTwoWeekLow
    )
}


fun SnRLevelsEntity.toSnRLevels(): SnRLevels{
    return SnRLevels(
        ticker, r3!!, r2!!, r1!!, approxPrice!!, s1!!, s2!!, s3!!, fiftyTwoWeekHigh!!, fibonacci62Pct!!, 
        fibonacci50Pct!!, fibonacci38Pct!!, fiftyTwoWeekLow!!,
    )
}


@Entity(tableName = DbConstants.S_N_R_TABLE)
data class SnRLevelsEntity(
    @PrimaryKey val ticker: String = "NONE",
    @ColumnInfo val timeSaved: Long = System.currentTimeMillis(),
    @ColumnInfo val r3: Double? = null,
    @ColumnInfo val r2: Double? = null,
    @ColumnInfo val r1: Double? = null,
    @ColumnInfo val approxPrice: Double? = null,
    @ColumnInfo val s1: Double? = null,
    @ColumnInfo val s2: Double? = null,
    @ColumnInfo val s3: Double? = null,
    @ColumnInfo val fiftyTwoWeekHigh: Double? = null,
    @ColumnInfo val fibonacci62Pct: Double? = null,
    @ColumnInfo val fibonacci50Pct: Double? = null,
    @ColumnInfo val fibonacci38Pct: Double? = null,
    @ColumnInfo val fiftyTwoWeekLow: Double? = null,
)