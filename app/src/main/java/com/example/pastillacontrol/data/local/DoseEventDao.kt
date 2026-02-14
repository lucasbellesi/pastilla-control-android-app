package com.example.pastillacontrol.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DoseEventDao {
    @Query("SELECT * FROM dose_events ORDER BY scheduledAtEpochMillis DESC")
    fun observeAll(): Flow<List<DoseEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(doseEvent: DoseEventEntity): Long

    @Query(
        "UPDATE dose_events SET status = :status, takenAtEpochMillis = :takenAt, takenByUserId = :takenBy WHERE id = :doseEventId"
    )
    suspend fun markTaken(doseEventId: Long, status: String, takenAt: Long, takenBy: String)
}
