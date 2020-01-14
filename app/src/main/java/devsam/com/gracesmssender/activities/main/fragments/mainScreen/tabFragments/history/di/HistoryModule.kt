package devsam.com.gracesmssender.activities.main.fragments.mainScreen.tabFragments.history.di

import devsam.com.gracesmssender.activities.main.fragments.mainScreen.tabFragments.history.viewModel.HistoryFragmentViewModel
import devsam.com.gracesmssender.backend.MyCustomApplication
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val historyModule = module {

    viewModel {
        HistoryFragmentViewModel(
            androidApplication() as MyCustomApplication,
            get(),
            get()
        )
    }

}