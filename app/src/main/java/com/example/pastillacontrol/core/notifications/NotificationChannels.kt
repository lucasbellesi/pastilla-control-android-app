package com.example.pastillacontrol.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannels {
    const val REMINDERS = "reminders"
    const val ESCALATIONS = "escalations"

    fun create(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = context.getSystemService(NotificationManager::class.java)

        val reminderChannel = NotificationChannel(
            REMINDERS,
            "Medication reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Scheduled reminders to take medication"
        }

        val escalationChannel = NotificationChannel(
            ESCALATIONS,
            "Missed dose escalation",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Alerts when a dose is missed"
        }

        manager.createNotificationChannels(listOf(reminderChannel, escalationChannel))
    }
}
