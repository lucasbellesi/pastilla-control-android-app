package com.example.pastillacontrol.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val name: String,
    val dosageAmount: String,
    val dosageUnit: String,
    val notes: String,
    val isActive: Boolean = true,
    val createdAtEpochMillis: Long
)
