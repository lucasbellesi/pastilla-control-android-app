package com.example.pastillacontrol.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EscalationPolicyDao {
    @Query("SELECT * FROM escalation_policies WHERE patientUserId = :patientUserId LIMIT 1")
    fun observeByPatient(patientUserId: String): Flow<EscalationPolicyEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(policy: EscalationPolicyEntity): Long
}
