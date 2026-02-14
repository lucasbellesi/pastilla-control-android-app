package com.example.pastillacontrol.ui.medications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pastillacontrol.data.local.AppDatabaseProvider
import com.example.pastillacontrol.data.local.MedicationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicationEditorViewModel(application: Application) : AndroidViewModel(application) {
    private val medicationDao = AppDatabaseProvider.get(application).medicationDao()

    val medication = MutableLiveData<MedicationEntity?>()
    val saveCompleted = MutableLiveData<Boolean>()

    fun loadMedication(id: Long) {
        if (id <= 0L) {
            medication.value = null
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            medication.postValue(medicationDao.getById(id))
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
            val userId = "local_patient"
            if (id > 0L) {
                medicationDao.update(
                    MedicationEntity(
                        id = id,
                        userId = userId,
                        name = name,
                        dosageAmount = dosageAmount,
                        dosageUnit = dosageUnit,
                        notes = notes,
                        isActive = true,
                        createdAtEpochMillis = medication.value?.createdAtEpochMillis
                            ?: System.currentTimeMillis()
                    )
                )
            } else {
                medicationDao.insert(
                    MedicationEntity(
                        userId = userId,
                        name = name,
                        dosageAmount = dosageAmount,
                        dosageUnit = dosageUnit,
                        notes = notes,
                        isActive = true,
                        createdAtEpochMillis = System.currentTimeMillis()
                    )
                )
            }
            saveCompleted.postValue(true)
        }
    }
}
