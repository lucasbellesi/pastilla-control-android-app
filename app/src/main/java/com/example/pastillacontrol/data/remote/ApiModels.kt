package com.example.pastillacontrol.data.remote

data class RegisterRequest(
    val email: String,
    val full_name: String,
    val password: String,
    val role: String = "PATIENT"
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class TokenResponse(
    val access_token: String,
    val token_type: String
)

data class MedicationPayload(
    val name: String,
    val dosage_amount: String,
    val dosage_unit: String,
    val notes: String?,
    val is_active: Boolean = true
)

data class MedicationResponse(
    val id: Long,
    val name: String,
    val dosage_amount: String,
    val dosage_unit: String,
    val notes: String?,
    val is_active: Boolean
)

data class SchedulePayload(
    val medication_id: Long,
    val type: String,
    val time_of_day: String,
    val days_of_week_mask: Int,
    val interval_hours: Int?,
    val timezone_id: String,
    val grace_minutes: Int,
    val is_active: Boolean = true
)

data class ScheduleResponse(
    val id: Long,
    val medication_id: Long,
    val type: String,
    val time_of_day: String,
    val days_of_week_mask: Int,
    val interval_hours: Int?,
    val timezone_id: String,
    val grace_minutes: Int,
    val is_active: Boolean
)
