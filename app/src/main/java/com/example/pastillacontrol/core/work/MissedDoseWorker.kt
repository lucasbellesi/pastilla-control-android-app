package com.example.pastillacontrol.core.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class MissedDoseWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        // Placeholder for grace-window expiry checks and escalation.
        return Result.success()
    }
}
