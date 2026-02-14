package com.example.pastillacontrol.ui.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.pastillacontrol.data.local.AppDatabaseProvider
import com.example.pastillacontrol.data.local.ScheduleEntity
import java.util.TimeZone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleEditorViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabaseProvider.get(application)
    private val medicationDao = db.medicationDao()
    private val scheduleDao = db.scheduleDao()

    val medications = medicationDao.observeAll().asLiveData()
    val schedules = scheduleDao.observeAll().asLiveData()

    fun saveSchedule(
        medicationId: Long,
        type: String,
        timeOfDay: String,
        daysOfWeekMask: Int,
        intervalHours: Int?,
        graceMinutes: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleDao.insert(
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
        }
    }

    fun deleteSchedule(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleDao.deleteById(id)
        }
    }
}
