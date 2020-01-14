package devsam.com.gracesmssender.activities.sendBulkSms.data

import kotlinx.serialization.Serializable



fun String.toSmsContact() =
    SmsContact(this)

@Serializable
data class SmsContact(
    val contactNumber: String,
    var isSent: Boolean = false
)
