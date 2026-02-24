package com.example.pastillacontrol.data.remote

interface FamilyNotificationRemoteDataSource {
    suspend fun notifyMissedDose(doseEventId: Long)
}
