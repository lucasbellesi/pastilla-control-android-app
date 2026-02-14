package com.example.pastillacontrol.ui.medications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.pastillacontrol.data.local.AppDatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicationsViewModel(application: Application) : AndroidViewModel(application) {
    private val medicationDao = AppDatabaseProvider.get(application).medicationDao()

    val medications = medicationDao.observeAll().asLiveData()

    fun deleteMedication(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            medicationDao.deleteById(id)
        }
    }
}
