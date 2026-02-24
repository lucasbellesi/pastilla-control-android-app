package com.example.pastillacontrol

import android.app.Application
import com.example.pastillacontrol.core.notifications.NotificationChannels

class PastillaControlApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationChannels.create(this)
    }
}
