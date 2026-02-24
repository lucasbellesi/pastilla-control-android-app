package com.example.pastillacontrol.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AcknowledgementDao {
    @Query("SELECT * FROM acknowledgements WHERE doseEventId = :doseEventId ORDER BY timestampEpochMillis DESC")
    fun observeByDoseEvent(doseEventId: Long): Flow<List<AcknowledgementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ack: AcknowledgementEntity): Long
}
