package com.example.pastillacontrol.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FamilyMemberDao {
    @Query("SELECT * FROM family_members WHERE patientUserId = :patientUserId ORDER BY priorityOrder ASC")
    fun observeByPatient(patientUserId: String): Flow<List<FamilyMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(member: FamilyMemberEntity): Long
}
