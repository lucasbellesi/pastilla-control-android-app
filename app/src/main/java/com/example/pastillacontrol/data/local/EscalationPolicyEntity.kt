package com.example.pastillacontrol.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "escalation_policies")
data class EscalationPolicyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientUserId: String,
    val enabled: Boolean = true,
    val stepDelayMinutes: Int,
    val maxSteps: Int,
    val channelMask: Int
)
