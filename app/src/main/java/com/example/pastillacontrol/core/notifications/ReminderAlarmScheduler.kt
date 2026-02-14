package com.example.pastillacontrol.core.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class ReminderAlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun scheduleDoseReminder(doseEventId: Long, triggerAtMillis: Long) {
        val intent = Intent(context, MedicationReminderReceiver::class.java).apply {
            action = MedicationReminderReceiver.ACTION_SHOW_REMINDER
            putExtra(MedicationReminderReceiver.EXTRA_DOSE_EVENT_ID, doseEventId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            doseEventId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }
}
