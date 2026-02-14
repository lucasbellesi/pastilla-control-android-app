package com.example.pastillacontrol.data.local

import android.content.Context

class DoseEventActionStore(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun markTaken(doseEventId: Long) {
        sharedPreferences.edit()
            .putLong(KEY_LAST_TAKEN_DOSE_EVENT_ID, doseEventId)
            .putLong(KEY_LAST_TAKEN_AT, System.currentTimeMillis())
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "dose_event_actions"
        private const val KEY_LAST_TAKEN_DOSE_EVENT_ID = "last_taken_dose_event_id"
        private const val KEY_LAST_TAKEN_AT = "last_taken_at"
    }
}
