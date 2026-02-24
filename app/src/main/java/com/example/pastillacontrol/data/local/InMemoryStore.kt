package com.example.pastillacontrol.data.local

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

object InMemoryStore {
    private const val PREFS = "pastilla_local_store"
    private const val KEY_MEDICATIONS = "medications"
    private const val KEY_SCHEDULES = "schedules"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        }
    }

    private fun requirePrefs(): SharedPreferences {
        check(::prefs.isInitialized) { "InMemoryStore.init(context) must be called first" }
        return prefs
    }

    private fun medicationsFromPrefs(): MutableList<MedicationEntity> {
        val json = requirePrefs().getString(KEY_MEDICATIONS, "[]") ?: "[]"
        val array = JSONArray(json)
        val result = mutableListOf<MedicationEntity>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            result.add(
                MedicationEntity(
                    id = obj.getLong("id"),
                    userId = obj.getString("userId"),
                    name = obj.getString("name"),
                    dosageAmount = obj.getString("dosageAmount"),
                    dosageUnit = obj.getString("dosageUnit"),
                    notes = obj.getString("notes"),
                    isActive = obj.getBoolean("isActive"),
                    createdAtEpochMillis = obj.getLong("createdAtEpochMillis")
                )
            )
        }
        return result
    }

    private fun schedulesFromPrefs(): MutableList<ScheduleEntity> {
        val json = requirePrefs().getString(KEY_SCHEDULES, "[]") ?: "[]"
        val array = JSONArray(json)
        val result = mutableListOf<ScheduleEntity>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            result.add(
                ScheduleEntity(
                    id = obj.getLong("id"),
                    medicationId = obj.getLong("medicationId"),
                    type = obj.getString("type"),
                    timeOfDay = obj.getString("timeOfDay"),
                    daysOfWeekMask = obj.getInt("daysOfWeekMask"),
                    intervalHours = if (obj.isNull("intervalHours")) null else obj.getInt("intervalHours"),
                    startDateEpochMillis = obj.getLong("startDateEpochMillis"),
                    timezoneId = obj.getString("timezoneId"),
                    graceMinutes = obj.getInt("graceMinutes"),
                    isActive = obj.getBoolean("isActive")
                )
            )
        }
        return result
    }

    private fun persistMedications(items: List<MedicationEntity>) {
        val array = JSONArray()
        items.forEach {
            array.put(
                JSONObject()
                    .put("id", it.id)
                    .put("userId", it.userId)
                    .put("name", it.name)
                    .put("dosageAmount", it.dosageAmount)
                    .put("dosageUnit", it.dosageUnit)
                    .put("notes", it.notes)
                    .put("isActive", it.isActive)
                    .put("createdAtEpochMillis", it.createdAtEpochMillis)
            )
        }
        requirePrefs().edit().putString(KEY_MEDICATIONS, array.toString()).apply()
    }

    private fun persistSchedules(items: List<ScheduleEntity>) {
        val array = JSONArray()
        items.forEach {
            array.put(
                JSONObject()
                    .put("id", it.id)
                    .put("medicationId", it.medicationId)
                    .put("type", it.type)
                    .put("timeOfDay", it.timeOfDay)
                    .put("daysOfWeekMask", it.daysOfWeekMask)
                    .put("intervalHours", it.intervalHours)
                    .put("startDateEpochMillis", it.startDateEpochMillis)
                    .put("timezoneId", it.timezoneId)
                    .put("graceMinutes", it.graceMinutes)
                    .put("isActive", it.isActive)
            )
        }
        requirePrefs().edit().putString(KEY_SCHEDULES, array.toString()).apply()
    }

    private fun nextMedicationId(items: List<MedicationEntity>): Long = (items.maxOfOrNull { it.id } ?: 0L) + 1L

    private fun nextScheduleId(items: List<ScheduleEntity>): Long = (items.maxOfOrNull { it.id } ?: 0L) + 1L

    @Synchronized
    fun getMedications(): List<MedicationEntity> = medicationsFromPrefs()

    @Synchronized
    fun getMedicationById(id: Long): MedicationEntity? = medicationsFromPrefs().firstOrNull { it.id == id }

    @Synchronized
    fun upsertMedication(medication: MedicationEntity): MedicationEntity {
        val medications = medicationsFromPrefs()
        val stored = if (medication.id <= 0L) {
            medication.copy(id = nextMedicationId(medications))
        } else {
            medication
        }

        val index = medications.indexOfFirst { it.id == stored.id }
        if (index >= 0) {
            medications[index] = stored
        } else {
            medications.add(stored)
        }

        persistMedications(medications)

        return stored
    }

    @Synchronized
    fun deleteMedication(id: Long) {
        val medications = medicationsFromPrefs()
        val schedules = schedulesFromPrefs()
        medications.removeAll { it.id == id }
        schedules.removeAll { it.medicationId == id }
        persistMedications(medications)
        persistSchedules(schedules)
    }

    @Synchronized
    fun getSchedules(): List<ScheduleEntity> = schedulesFromPrefs()

    @Synchronized
    fun addSchedule(schedule: ScheduleEntity): ScheduleEntity {
        val schedules = schedulesFromPrefs()
        val stored = schedule.copy(id = nextScheduleId(schedules))
        schedules.add(stored)
        persistSchedules(schedules)
        return stored
    }

    @Synchronized
    fun deleteSchedule(id: Long) {
        val schedules = schedulesFromPrefs()
        schedules.removeAll { it.id == id }
        persistSchedules(schedules)
    }
}
