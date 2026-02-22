package com.auris

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * AurisApp — Application class
 * Hilt DI graph root. Also initialises Timber logging for debug builds.
 */
@HiltAndroidApp
class AurisApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
