package com.willor.lib_data.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.willor.ktstockdata.marketdata.dataobjects.MajorIndicesData
import com.willor.lib_data.utils.DbConstants

fun MajorIndicesData.toMajorIndexDataEntity(): MajorIndicesDataEntity{
    return MajorIndicesDataEntity(
        data = Gson().toJson(this)
    )
}


fun MajorIndicesDataEntity.toMajorIndicesData(): MajorIndicesData{
    return Gson().fromJson(this.data, MajorIndicesData::class.java)
}


@Entity(tableName = DbConstants.MAJOR_INDICES_TABLE)
data class MajorIndicesDataEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo val timeSaved: Long = System.currentTimeMillis(),
    @ColumnInfo val data: String = ""
)