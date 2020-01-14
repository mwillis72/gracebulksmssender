package devsam.com.gracesmssender.di

import android.content.Context
import devsam.com.gracesmssender.helper.SharedPreferenceHelper
import devsam.com.gracesmssender.helper.UiHelper
import org.koin.dsl.module


private const val USER_SHARED_PREFERENCE = "devsam.com.gracesmssender_user_shared_preference"

val utilModule = module {

    single { UiHelper() }

    single {
        val sharedPreference =
            get<Context>().getSharedPreferences(USER_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        SharedPreferenceHelper(sharedPreference)
    }
}
