package com.example.pastillacontrol.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "family_members")
data class FamilyMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientUserId: String,
    val displayName: String,
    val relationship: String,
    val contactUserId: String?,
    val phone: String?,
    val priorityOrder: Int,
    val escalationEnabled: Boolean = true
)
