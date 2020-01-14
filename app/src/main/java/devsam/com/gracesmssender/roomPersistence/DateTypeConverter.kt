package devsam.com.gracesmssender.roomPersistence

import androidx.room.TypeConverter
import java.util.*



class DateTypeConverter {

    @TypeConverter
    fun toDate(value: Long): Date? = Date(value)

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time
}