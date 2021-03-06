package devsam.com.gracesmssender.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import devsam.com.gracesmssender.activities.sendBulkSms.ui.SendBulkSmsActivity


interface ActivityArgs {

    fun intent(context: Context): Intent

    fun launch(context: Context, options: Bundle? = null) =
        context.launch(intent = intent(context), options = options)

    fun launch(activity: Activity, options: Bundle? = null, requestCode: Int = -1) =
        activity.launch(intent = intent(activity), requestCode = requestCode, options = options)
}

class SendBulkSmsActivityArgs : ActivityArgs {

    override fun intent(context: Context) = Intent(context, SendBulkSmsActivity::class.java)
}