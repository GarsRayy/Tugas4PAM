package org.garis.pam

import android.app.Application
import org.garis.pam.di.commonModule
import org.garis.pam.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(commonModule, platformModule)
        }
    }
}
