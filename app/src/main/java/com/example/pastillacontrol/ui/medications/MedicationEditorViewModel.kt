package com.example.pastillacontrol.ui.medications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pastillacontrol.data.local.InMemoryStore
import com.example.pastillacontrol.data.local.MedicationEntity

class MedicationEditorViewModel(application: Application) : AndroidViewModel(application) {
    val medication = MutableLiveData<MedicationEntity?>()
    val saveCompleted = MutableLiveData<Boolean>()

    init {
        InMemoryStore.init(application)
    }

    fun loadMedication(id: Long) {
        if (id <= 0L) {
            medication.value = null
            return
        }

        medication.value = InMemoryStore.getMedicationById(id)
    }

    fun saveMedication(
        id: Long,
        name: String,
        dosageAmount: String,
        dosageUnit: String,
        notes: String
    ) {
        val userId = "local_patient"
        val existing = medication.value
        InMemoryStore.upsertMedication(
            MedicationEntity(
                id = id,
                userId = userId,
                name = name,
                dosageAmount = dosageAmount,
                dosageUnit = dosageUnit,
                notes = notes,
                isActive = true,
                createdAtEpochMillis = existing?.createdAtEpochMillis ?: System.currentTimeMillis()
            )
        )
        saveCompleted.value = true
    }
}
