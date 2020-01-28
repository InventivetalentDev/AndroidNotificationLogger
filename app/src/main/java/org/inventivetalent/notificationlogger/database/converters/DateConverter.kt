package org.inventivetalent.notificationlogger.database.converters

import androidx.room.TypeConverter
import java.util.*

object DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}
