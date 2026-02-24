package com.example.pastillacontrol.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medications ORDER BY name ASC")
    fun observeAll(): Flow<List<MedicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medication: MedicationEntity): Long

    @Update
    suspend fun update(medication: MedicationEntity)

    @Query("SELECT * FROM medications WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): MedicationEntity?

    @Query("DELETE FROM medications WHERE id = :id")
    suspend fun deleteById(id: Long)
}
