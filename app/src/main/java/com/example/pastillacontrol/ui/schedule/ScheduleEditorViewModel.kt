package com.example.pastillacontrol.ui.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pastillacontrol.data.local.MedicationEntity
import com.example.pastillacontrol.data.local.ScheduleEntity
import com.example.pastillacontrol.data.repository.BackendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleEditorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BackendRepository(application)
    private val _medications = MutableLiveData<List<MedicationEntity>>(emptyList())
    val medications: LiveData<List<MedicationEntity>> = _medications

    private val _schedules = MutableLiveData<List<ScheduleEntity>>(emptyList())
    val schedules: LiveData<List<ScheduleEntity>> = _schedules

    init {
        repository.initializeLocalStore()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            _medications.postValue(repository.listMedications())
            _schedules.postValue(repository.listSchedules())
        }
    }

    fun saveSchedule(
        medicationId: Long,
        type: String,
        timeOfDay: String,
        daysOfWeekMask: Int,
        intervalHours: Int?,
        graceMinutes: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createSchedule(
                repository.newSchedule(
                    medicationId = medicationId,
                    type = type,
                    timeOfDay = timeOfDay,
                    daysOfWeekMask = daysOfWeekMask,
                    intervalHours = intervalHours,
                    graceMinutes = graceMinutes
                )
            )
            _schedules.postValue(repository.listSchedules())
        }
    }

    fun deleteSchedule(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSchedule(id)
            _schedules.postValue(repository.listSchedules())
        }
    }
}
