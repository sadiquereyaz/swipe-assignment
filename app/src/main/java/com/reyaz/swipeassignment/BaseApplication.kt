package com.reyaz.swipeassignment

import android.app.Application
import androidx.work.Configuration
import com.reyaz.swipeassignment.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class BaseApplication : Application(), Configuration.Provider, KoinComponent {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(appModule)
        }
    }

    // Implement the workManagerConfiguration property
    override val workManagerConfiguration: Configuration
        get() = get() // Retrieve the Configuration instance from Koin
}