package com.willor.lib_data.data.local.local_db.utils

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun convertDateToLong(date: Date): Long{
        return date.time
    }

    @TypeConverter
    fun convertLongToDate(l: Long): Date{
        return Date(l)
    }

}