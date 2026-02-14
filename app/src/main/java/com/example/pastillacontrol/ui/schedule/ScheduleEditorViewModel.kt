package com.example.pastillacontrol.ui.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pastillacontrol.data.local.InMemoryStore
import com.example.pastillacontrol.data.local.MedicationEntity
import com.example.pastillacontrol.data.local.ScheduleEntity
import java.util.TimeZone

class ScheduleEditorViewModel(application: Application) : AndroidViewModel(application) {
    private val _medications = MutableLiveData<List<MedicationEntity>>(emptyList())
    val medications: LiveData<List<MedicationEntity>> = _medications

    private val _schedules = MutableLiveData<List<ScheduleEntity>>(emptyList())
    val schedules: LiveData<List<ScheduleEntity>> = _schedules

    init {
        InMemoryStore.init(application)
    }

    fun refresh() {
        _medications.value = InMemoryStore.getMedications()
        _schedules.value = InMemoryStore.getSchedules()
    }

    fun saveSchedule(
        medicationId: Long,
        type: String,
        timeOfDay: String,
        daysOfWeekMask: Int,
        intervalHours: Int?,
        graceMinutes: Int
    ) {
        InMemoryStore.addSchedule(
            ScheduleEntity(
                medicationId = medicationId,
                type = type,
                timeOfDay = timeOfDay,
                daysOfWeekMask = daysOfWeekMask,
                intervalHours = intervalHours,
                startDateEpochMillis = System.currentTimeMillis(),
                timezoneId = TimeZone.getDefault().id,
                graceMinutes = graceMinutes,
                isActive = true
            )
        )
        refresh()
    }

    fun deleteSchedule(id: Long) {
        InMemoryStore.deleteSchedule(id)
        refresh()
    }
}
