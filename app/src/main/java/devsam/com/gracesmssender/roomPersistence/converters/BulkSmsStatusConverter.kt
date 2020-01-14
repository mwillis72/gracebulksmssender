package devsam.com.gracesmssender.roomPersistence.converters

import androidx.room.TypeConverter
import devsam.com.gracesmssender.roomPersistence.BulkSmsStatus


object BulkSmsStatusConverter {

    @JvmStatic
    @TypeConverter
    fun toSmsStatus(json : String) : BulkSmsStatus {
        return BulkSmsStatus.valueOf(json)
    }

    @JvmStatic
    @TypeConverter
    fun toString(status : BulkSmsStatus) : String {
        return status.name
    }
}