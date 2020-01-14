package devsam.com.gracesmssender.roomPersistence.converters

import androidx.room.TypeConverter
import devsam.com.gracesmssender.activities.sendBulkSms.data.SmsContact
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.list



@UnstableDefault
object CollectionTypeConverter {

    @TypeConverter
    @JvmStatic
    fun toCollectionString(list: List<SmsContact>): String {
        return Json.stringify(SmsContact.serializer().list, list)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringToCollection(json: String): List<SmsContact> {
        return Json.parse(SmsContact.serializer().list, json)
    }
}