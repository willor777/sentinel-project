package com.willor.lib_data.data.local.db.utils

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun convertDateToLong(date: Date?): Long{
        if (date == null){
            return 0
        }
        return date.time
    }

    @TypeConverter
    fun convertLongToDate(l: Long): Date?{
        if (l == 0L){
            return null
        }
        return Date(l)
    }

}