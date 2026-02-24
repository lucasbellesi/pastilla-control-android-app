package com.example.pastillacontrol.core.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DeviceStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED -> {
                // Placeholder for rescheduling reminders after reboot/time changes.
            }
            else -> return
        }
    }
}
