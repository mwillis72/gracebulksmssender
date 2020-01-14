package devsam.com.gracesmssender.activities.sendBulkSms.di

import devsam.com.gracesmssender.activities.sendBulkSms.viewModel.SendBulkSmsViewModel
import devsam.com.gracesmssender.backend.MyCustomApplication
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module



val sendBulkSmsModule = module {

    viewModel { SendBulkSmsViewModel(androidApplication() as MyCustomApplication, get(), get()) }
}