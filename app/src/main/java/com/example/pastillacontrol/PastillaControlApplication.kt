package com.example.pastillacontrol

import android.app.Application
import com.example.pastillacontrol.core.localization.AppLanguageManager
import com.example.pastillacontrol.core.notifications.NotificationChannels

class PastillaControlApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppLanguageManager.applySavedLanguage(this)
        NotificationChannels.create(this)
    }
}
