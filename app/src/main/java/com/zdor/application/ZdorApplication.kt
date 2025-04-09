package com.zdor.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ZdorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
