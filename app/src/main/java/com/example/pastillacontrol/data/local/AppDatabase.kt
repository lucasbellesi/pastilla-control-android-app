package com.example.pastillacontrol.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        MedicationEntity::class,
        ScheduleEntity::class,
        DoseEventEntity::class,
        FamilyMemberEntity::class,
        EscalationPolicyEntity::class,
        AcknowledgementEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun doseEventDao(): DoseEventDao
    abstract fun familyMemberDao(): FamilyMemberDao
    abstract fun escalationPolicyDao(): EscalationPolicyDao
    abstract fun acknowledgementDao(): AcknowledgementDao
}
