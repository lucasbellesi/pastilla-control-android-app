package com.example.pastillacontrol.data.repository

interface DoseEventRepository {
    suspend fun markTaken(doseEventId: Long)
}
