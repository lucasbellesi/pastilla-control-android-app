package com.example.pastillacontrol.core.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.pastillacontrol.MainActivity
import com.example.pastillacontrol.R
import com.example.pastillacontrol.data.local.DoseEventActionStore

class MedicationReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val doseEventId = intent.getLongExtra(EXTRA_DOSE_EVENT_ID, -1L)
        if (doseEventId <= 0L) return

        when (intent.action) {
            ACTION_SHOW_REMINDER -> showReminderNotification(context, doseEventId)
            ACTION_MARK_TAKEN -> markTaken(context, doseEventId)
        }
    }

    private fun showReminderNotification(context: Context, doseEventId: Long) {
        val takenIntent = Intent(context, MedicationReminderReceiver::class.java).apply {
            action = ACTION_MARK_TAKEN
            putExtra(EXTRA_DOSE_EVENT_ID, doseEventId)
        }

        val takenPendingIntent = PendingIntent.getBroadcast(
            context,
            (doseEventId + 10_000L).toInt(),
            takenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val openAppIntent = PendingIntent.getActivity(
            context,
            doseEventId.toInt(),
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NotificationChannels.REMINDERS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.reminder_title))
            .setContentText(context.getString(R.string.reminder_message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(openAppIntent)
            .addAction(0, context.getString(R.string.action_taken), takenPendingIntent)
            .build()

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(context).notify(doseEventId.toInt(), notification)
    }

    private fun markTaken(context: Context, doseEventId: Long) {
        DoseEventActionStore(context).markTaken(doseEventId)
        NotificationManagerCompat.from(context).cancel(doseEventId.toInt())
    }

    companion object {
        const val ACTION_SHOW_REMINDER = "com.example.pastillacontrol.action.SHOW_REMINDER"
        const val ACTION_MARK_TAKEN = "com.example.pastillacontrol.action.MARK_TAKEN"
        const val EXTRA_DOSE_EVENT_ID = "extra_dose_event_id"
    }
}
