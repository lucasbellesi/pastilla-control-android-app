package com.example.pastillacontrol.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "acknowledgements",
    foreignKeys = [
        ForeignKey(
            entity = DoseEventEntity::class,
            parentColumns = ["id"],
            childColumns = ["doseEventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("doseEventId")]
)
data class AcknowledgementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val doseEventId: Long,
    val actorUserId: String,
    val actorRole: String,
    val action: String,
    val timestampEpochMillis: Long,
    val note: String?
)
