package com.example.pastillacontrol.ui.medications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pastillacontrol.data.local.MedicationEntity
import com.example.pastillacontrol.data.repository.BackendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicationsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BackendRepository(application)
    private val _medications = MutableLiveData<List<MedicationEntity>>(emptyList())
    val medications: LiveData<List<MedicationEntity>> = _medications

    init {
        repository.initializeLocalStore()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _medications.postValue(repository.listMedications())
        }
    }

    fun deleteMedication(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMedication(id)
            _medications.postValue(repository.listMedications())
        }
    }
}
