package org.garis.pam

import android.app.Application
import org.garis.pam.di.dataModule
import org.garis.pam.di.viewModelModule
import org.garis.pam.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            startKoin {
                androidContext(this@MainApplication)
                modules(dataModule, viewModelModule, platformModule)
            }
        } catch (e: Exception) {
            // Already started
        }
    }
}
