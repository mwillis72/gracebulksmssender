package devsam.com.gracesmssender.di

import devsam.com.gracesmssender.roomPersistence.BulkSmsDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module



val roomModule = module {

    single {
        BulkSmsDatabase.getInstance(androidContext())
    }

    single {
        get<BulkSmsDatabase>().bulkSmsDao()
    }
}