package com.example.pastillacontrol.ui.medications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pastillacontrol.data.local.MedicationEntity
import com.example.pastillacontrol.data.repository.BackendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicationEditorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BackendRepository(application)
    val medication = MutableLiveData<MedicationEntity?>()
    val saveCompleted = MutableLiveData<Boolean>()

    init {
        repository.initializeLocalStore()
    }

    fun loadMedication(id: Long) {
        if (id <= 0L) {
            medication.value = null
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            medication.postValue(repository.getMedicationById(id))
        }
    }

    fun saveMedication(
        id: Long,
        name: String,
        dosageAmount: String,
        dosageUnit: String,
        notes: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = "remote"
            val existing = medication.value
            repository.upsertMedication(
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
            saveCompleted.postValue(true)
        }
    }
}
