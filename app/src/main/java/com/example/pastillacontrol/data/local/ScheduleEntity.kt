package com.example.pastillacontrol.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedules",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("medicationId")]
)
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medicationId: Long,
    val type: String,
    val timeOfDay: String,
    val daysOfWeekMask: Int,
    val intervalHours: Int?,
    val startDateEpochMillis: Long,
    val timezoneId: String,
    val graceMinutes: Int,
    val isActive: Boolean = true
)
