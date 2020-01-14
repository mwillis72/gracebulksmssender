package devsam.com.gracesmssender.activities.main.fragments.mainScreen.tabFragments.settings.di

import devsam.com.gracesmssender.activities.main.fragments.mainScreen.tabFragments.settings.viewModel.SettingsFragmentViewModel
import devsam.com.gracesmssender.backend.MyCustomApplication
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val settingsModule = module {

    viewModel { SettingsFragmentViewModel(androidApplication() as MyCustomApplication, get()) }
}