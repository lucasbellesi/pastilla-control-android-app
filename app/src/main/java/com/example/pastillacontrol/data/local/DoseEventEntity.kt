package com.example.pastillacontrol.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dose_events",
    foreignKeys = [
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ScheduleEntity::class,
            parentColumns = ["id"],
            childColumns = ["scheduleId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("medicationId"), Index("scheduleId")]
)
data class DoseEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medicationId: Long,
    val scheduleId: Long,
    val scheduledAtEpochMillis: Long,
    val reminderSentAtEpochMillis: Long?,
    val status: String,
    val takenAtEpochMillis: Long?,
    val takenByUserId: String?,
    val graceDeadlineAtEpochMillis: Long
)
