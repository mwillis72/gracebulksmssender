package devsam.com.gracesmssender.activities.main.fragments.mainScreen.tabFragments.history.viewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import devsam.com.gracesmssender.backend.MyCustomApplication
import devsam.com.gracesmssender.helper.SharedPreferenceHelper
import devsam.com.gracesmssender.helper.SharedPreferenceHelper.Companion.BULKS_SMS_PREVIOUS_WORKER_ID
import devsam.com.gracesmssender.roomPersistence.BulkSms
import devsam.com.gracesmssender.roomPersistence.BulkSmsDao
import devsam.com.gracesmssender.roomPersistence.BulkSmsStatus
import devsam.com.gracesmssender.util.cancelWorker
import devsam.com.gracesmssender.util.notificationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch




class HistoryFragmentViewModel constructor(
    application: MyCustomApplication,
    private val bulkSmsDao: BulkSmsDao,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : AndroidViewModel(application) {

    private val coroutineContext = Dispatchers.Default + SupervisorJob()

    val allTasks: LiveData<List<BulkSms>> = bulkSmsDao.all()

    fun deleteBulkSms(bulkSms: BulkSms) {
        viewModelScope.launch(coroutineContext) {
            bulkSmsDao.delete(bulkSms)
        }
    }

    fun cancelBulkSmsOperation(bulkSms: BulkSms) {
        viewModelScope.launch {
            val previousWorkerId = sharedPreferenceHelper.getString(BULKS_SMS_PREVIOUS_WORKER_ID)
            previousWorkerId?.let {
                val application = getApplication<MyCustomApplication>()
                application.cancelWorker(it)
                bulkSmsDao.update(System.currentTimeMillis(), BulkSmsStatus.CANCELLED, bulkSms.id)
                sharedPreferenceHelper.put(BULKS_SMS_PREVIOUS_WORKER_ID, null)
                application.notificationManager.cancelAll()
            }
        }
    }
}