package com.willor.lib_data.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.willor.lib_data.utils.DbConstants
import com.willor.sentinel_bots.domain.models.TriggerBase
import java.util.*


fun TriggerBase.toTriggerEntity(): TriggerEntity{
    // Build trigger entity
    return TriggerEntity(
        ticker = ticker,
        triggerValue = triggerValue,
        rating = rating,
        strategyName = strategyName,
        strategyDescription = strategyDescription,
        priceOfAsset = priceOfAsset,
        volumeOfTriggerCandle = volumeOfTriggerCandle,
    )
}


fun TriggerEntity.toTriggerBase(): TriggerBase{
    return TriggerBase(
        timestamp = timestamp,
        ticker = ticker,
        triggerValue = triggerValue,
        rating = rating,
        strategyName = strategyName,
        strategyDescription = strategyDescription,
        priceOfAsset = priceOfAsset,
        volumeOfTriggerCandle = volumeOfTriggerCandle
    )
}


@Entity(tableName = DbConstants.TRIGGER_TABLE)
data class TriggerEntity(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // Basics
    @ColumnInfo val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo val ticker: String,
    @ColumnInfo val triggerValue: Int,
    @ColumnInfo val rating: Int,
    @ColumnInfo val strategyName: String,
    @ColumnInfo val strategyDescription: String,
    @ColumnInfo val priceOfAsset: Double,
    @ColumnInfo val volumeOfTriggerCandle: Int,
)
