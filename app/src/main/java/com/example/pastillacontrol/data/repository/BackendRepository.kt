package com.example.pastillacontrol.data.repository

import android.content.Context
import com.example.pastillacontrol.data.local.InMemoryStore
import com.example.pastillacontrol.data.local.MedicationEntity
import com.example.pastillacontrol.data.local.ScheduleEntity
import com.example.pastillacontrol.data.remote.BackendApi
import com.example.pastillacontrol.data.remote.BackendClient
import com.example.pastillacontrol.data.remote.LoginRequest
import com.example.pastillacontrol.data.remote.MedicationPayload
import com.example.pastillacontrol.data.remote.RegisterRequest
import com.example.pastillacontrol.data.remote.SchedulePayload
import com.example.pastillacontrol.data.remote.SessionStore
import java.util.TimeZone
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Response

class BackendRepository(context: Context) {
    private val appContext = context.applicationContext
    private val api: BackendApi = BackendClient.create(appContext)
    private val sessionStore = SessionStore(appContext)
    private val authMutex = Mutex()

    suspend fun listMedications(): List<MedicationEntity> {
        return runCatching {
            val response = executeAuthorized { api.listMedications() }
            if (!response.isSuccessful) error("list medications failed")
            response.body().orEmpty().map {
                MedicationEntity(
                    id = it.id,
                    userId = "remote",
                    name = it.name,
                    dosageAmount = it.dosage_amount,
                    dosageUnit = it.dosage_unit,
                    notes = it.notes.orEmpty(),
                    isActive = it.is_active,
                    createdAtEpochMillis = System.currentTimeMillis()
                )
            }
        }.getOrElse {
            InMemoryStore.getMedications()
        }
    }

    suspend fun upsertMedication(entity: MedicationEntity) {
        runCatching {
            val payload = MedicationPayload(
                name = entity.name,
                dosage_amount = entity.dosageAmount,
                dosage_unit = entity.dosageUnit,
                notes = entity.notes,
                is_active = entity.isActive
            )

            val response = executeAuthorized {
                if (entity.id > 0L) {
                    api.updateMedication(entity.id, payload)
                } else {
                    api.createMedication(payload)
                }
            }

            if (!response.isSuccessful) error("upsert medication failed")
        }.onFailure {
            InMemoryStore.upsertMedication(entity)
        }
    }

    suspend fun deleteMedication(id: Long) {
        runCatching {
            val response = executeAuthorized { api.deleteMedication(id) }
            if (!response.isSuccessful) error("delete medication failed")
        }.onFailure {
            InMemoryStore.deleteMedication(id)
        }
    }

    suspend fun getMedicationById(id: Long): MedicationEntity? {
        return listMedications().firstOrNull { it.id == id }
    }

    suspend fun listSchedules(): List<ScheduleEntity> {
        return runCatching {
            val response = executeAuthorized { api.listSchedules() }
            if (!response.isSuccessful) error("list schedules failed")
            response.body().orEmpty().map {
                ScheduleEntity(
                    id = it.id,
                    medicationId = it.medication_id,
                    type = it.type,
                    timeOfDay = it.time_of_day,
                    daysOfWeekMask = it.days_of_week_mask,
                    intervalHours = it.interval_hours,
                    startDateEpochMillis = System.currentTimeMillis(),
                    timezoneId = it.timezone_id,
                    graceMinutes = it.grace_minutes,
                    isActive = it.is_active
                )
            }
        }.getOrElse {
            InMemoryStore.getSchedules()
        }
    }

    suspend fun createSchedule(schedule: ScheduleEntity) {
        runCatching {
            val payload = SchedulePayload(
                medication_id = schedule.medicationId,
                type = schedule.type,
                time_of_day = schedule.timeOfDay,
                days_of_week_mask = schedule.daysOfWeekMask,
                interval_hours = schedule.intervalHours,
                timezone_id = schedule.timezoneId,
                grace_minutes = schedule.graceMinutes,
                is_active = schedule.isActive
            )
            val response = executeAuthorized { api.createSchedule(payload) }
            if (!response.isSuccessful) error("create schedule failed")
        }.onFailure {
            InMemoryStore.addSchedule(schedule)
        }
    }

    suspend fun deleteSchedule(id: Long) {
        // No delete endpoint yet in backend scaffold; keep local-only fallback for now.
        InMemoryStore.deleteSchedule(id)
    }

    private suspend fun ensureAuthenticated(forceRefresh: Boolean = false) {
        if (!forceRefresh && !sessionStore.getToken().isNullOrBlank()) return

        authMutex.withLock {
            if (!forceRefresh && !sessionStore.getToken().isNullOrBlank()) return

            if (forceRefresh) {
                sessionStore.clearToken()
            }

            val email = sessionStore.getOrCreateDemoEmail()
            val password = sessionStore.getOrCreateDemoPassword()

            val login = api.login(LoginRequest(email, password))
            if (login.isSuccessful) {
                login.body()?.access_token?.let(sessionStore::saveToken)
                if (!sessionStore.getToken().isNullOrBlank()) return
            }

            val register = api.register(
                RegisterRequest(
                    email = email,
                    full_name = "Pastilla Demo User",
                    password = password,
                    role = "PATIENT"
                )
            )
            if (register.isSuccessful) {
                register.body()?.access_token?.let(sessionStore::saveToken)
            }
        }
    }

    private suspend fun <T> executeAuthorized(request: suspend () -> Response<T>): Response<T> {
        ensureAuthenticated()
        var response = request()

        if (response.code() == 401) {
            ensureAuthenticated(forceRefresh = true)
            response = request()
        }

        return response
    }

    fun initializeLocalStore() {
        InMemoryStore.init(appContext)
    }

    fun newSchedule(
        medicationId: Long,
        type: String,
        timeOfDay: String,
        daysOfWeekMask: Int,
        intervalHours: Int?,
        graceMinutes: Int
    ): ScheduleEntity {
        return ScheduleEntity(
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
    }
}
