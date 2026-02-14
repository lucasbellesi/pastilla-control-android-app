package com.example.pastillacontrol.ui.medications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pastillacontrol.data.local.InMemoryStore
import com.example.pastillacontrol.data.local.MedicationEntity

class MedicationsViewModel(application: Application) : AndroidViewModel(application) {
    private val _medications = MutableLiveData<List<MedicationEntity>>(emptyList())
    val medications: LiveData<List<MedicationEntity>> = _medications

    init {
        InMemoryStore.init(application)
    }

    fun refresh() {
        _medications.value = InMemoryStore.getMedications()
    }

    fun deleteMedication(id: Long) {
        InMemoryStore.deleteMedication(id)
        refresh()
    }
}
