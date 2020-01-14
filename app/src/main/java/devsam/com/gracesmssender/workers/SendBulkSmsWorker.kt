package devsam.com.gracesmssender.workers

import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.telephony.SmsManager
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import devsam.com.gracesmssender.R
import devsam.com.gracesmssender.backend.MyCustomApplication.Companion.APPLICATION_CHANNEL
import devsam.com.gracesmssender.helper.NotificationIdHelper
import devsam.com.gracesmssender.helper.SharedPreferenceHelper
import devsam.com.gracesmssender.helper.SharedPreferenceHelper.Companion.BULKS_SMS_PREVIOUS_WORKER_ID
import devsam.com.gracesmssender.helper.SharedPreferenceHelper.Companion.BULK_SMS_MESSAGE_DELAY_SECONDS
import devsam.com.gracesmssender.helper.SharedPreferenceHelper.Companion.BULK_SMS_PREFERRED_CARRIER_NUMBER
import devsam.com.gracesmssender.roomPersistence.BulkSmsDao
import devsam.com.gracesmssender.roomPersistence.BulkSmsStatus
import devsam.com.gracesmssender.util.Constants
import devsam.com.gracesmssender.util.notificationBuilder
import devsam.com.gracesmssender.util.notificationManager
import kotlinx.coroutines.delay
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber



class SendBulkSmsWorker constructor(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val bulkSmsDao: BulkSmsDao by inject()
    private val sharedPreferenceHelper: SharedPreferenceHelper by inject()
    private val notificationId = inputData.getInt(NOTIFICATION_ID, NotificationIdHelper.getId())
    private val subscriptionInfoId =
        sharedPreferenceHelper.getString(BULK_SMS_PREFERRED_CARRIER_NUMBER)?.split(
            Constants.CARRIER_NAME_SPLITTER
        )?.component1()?.toInt()
    private val smsManager =
        if (subscriptionInfoId != null) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SmsManager.getSmsManagerForSubscriptionId(subscriptionInfoId)
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP_MR1")
        } else SmsManager.getDefault()
    private val rowId = inputData.getLong(BULK_SMS_ROW_ID, -1)
    private val notificationManager = context.notificationManager
    private val notificationBuilder =
        context.notificationBuilder(APPLICATION_CHANNEL) {
            setSmallIcon(R.mipmap.ic_launcher_round)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            setContentTitle(SENDING_BULK_SMS)
            setAutoCancel(false)
            setOnlyAlertOnce(true)
            setOngoing(true)
        }

    private fun setNotificationCompleteStatus() {
        notificationBuilder.setProgress(0, 0, false)
            .setAutoCancel(true)
            .setOngoing(false)
            .setContentTitle("All sms have been sent").also {
                notificationManager.notify(notificationId, it.build())
            }
    }

    companion object {
        private const val SENDING_BULK_SMS = "Sending Bulk Sms"
        private const val BULK_SMS_ROW_ID =
            "devsam.com.gracesmssender.workers.SendBulkSmsWorker.BULK_SMS_ROW_ID"
        private const val NOTIFICATION_ID =
            "devsam.com.gracesmssender.workers.SendBulkSmsWorker.NOTIFICATION_ID"
        private const val SMS_CONTENT_LENGTH_LIMIT = 140

        fun constructWorkerParams(rowId: Long, notificationId: Int): Data =
            Data.Builder()
                .putLong(BULK_SMS_ROW_ID, rowId)
                .putInt(NOTIFICATION_ID, notificationId)
                .build()
    }

    override suspend fun doWork(): Result {
        Timber.e("Worker starts")
        val bulkSms = bulkSmsDao.bulkSmsWithRowId(rowId)
        val smsContacts = bulkSms.smsContacts
        val contactListSize = smsContacts.count()
        val remainSmsSentNumbers = smsContacts.filter { !it.isSent }
        var smsCountProgress = contactListSize - remainSmsSentNumbers.count()
        notificationManager.notify(
            notificationId, notificationBuilder.setProgress(
                contactListSize, smsCountProgress, false
            ).build()
        )
        val smsDelayValue =
            (sharedPreferenceHelper.getInt(BULK_SMS_MESSAGE_DELAY_SECONDS).toLong()) * 1000
        val smsContent = bulkSms.smsContent
        val smsDivides = smsManager.divideMessage(smsContent)
        val sourceSmsAddress = sharedPreferenceHelper.getString(BULK_SMS_PREFERRED_CARRIER_NUMBER)
        Timber.e("Source sms address -> $sourceSmsAddress and content -> $smsContent")
        for (smsContact in remainSmsSentNumbers) {
            if (smsContent.length < SMS_CONTENT_LENGTH_LIMIT)
                smsManager.sendTextMessage(
                    smsContact.contactNumber, null, smsContent, null, null
                )
            else
                smsManager.sendMultipartTextMessage(
                    smsContact.contactNumber, null, smsDivides, null, null
                )
            delay(smsDelayValue)
            notificationBuilder.setProgress(contactListSize, ++smsCountProgress, false)
                .setContentTitle(SENDING_BULK_SMS.plus(" $smsCountProgress/$contactListSize"))
                .also {
                    notificationManager.notify(notificationId, it.build())
                }
            smsContact.isSent = true
            bulkSmsDao.update(smsContacts, rowId)
        }
        setNotificationCompleteStatus()
        val endTime = System.currentTimeMillis()
        bulkSmsDao.update(endTime, BulkSmsStatus.COMPLETE, rowId)
        sharedPreferenceHelper.put(BULKS_SMS_PREVIOUS_WORKER_ID, null)
        Timber.e("Worker ends")
        return Result.success()
    }
}
