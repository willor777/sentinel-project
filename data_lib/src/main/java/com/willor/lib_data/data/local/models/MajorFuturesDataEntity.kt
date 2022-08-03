package com.willor.lib_data.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.willor.ktstockdata.misc_data.dataobjects.MajorFuturesData
import com.willor.lib_data.utils.DbConstants


fun MajorFuturesData.toMajorFuturesDataEntity(): MajorFuturesDataEntity{
    return MajorFuturesDataEntity(
        data = Gson().toJson(this)
    )
}


fun MajorFuturesDataEntity.toMajorFuturesData(): MajorFuturesData{
    return Gson().fromJson(this.data, MajorFuturesData::class.java)
}


@Entity(tableName = DbConstants.FUTURES_DATA_TABLE)
data class MajorFuturesDataEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo val timeSaved: Long = System.currentTimeMillis(),
    @ColumnInfo val data: String = "",
)