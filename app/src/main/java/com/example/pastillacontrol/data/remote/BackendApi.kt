package com.example.pastillacontrol.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BackendApi {
    @POST("auth/register")
    suspend fun register(@Body payload: RegisterRequest): Response<TokenResponse>

    @POST("auth/login")
    suspend fun login(@Body payload: LoginRequest): Response<TokenResponse>

    @GET("medications/")
    suspend fun listMedications(): Response<List<MedicationResponse>>

    @POST("medications/")
    suspend fun createMedication(@Body payload: MedicationPayload): Response<MedicationResponse>

    @PUT("medications/{id}")
    suspend fun updateMedication(
        @Path("id") id: Long,
        @Body payload: MedicationPayload
    ): Response<MedicationResponse>

    @DELETE("medications/{id}")
    suspend fun deleteMedication(@Path("id") id: Long): Response<Unit>

    @GET("schedules/")
    suspend fun listSchedules(): Response<List<ScheduleResponse>>

    @POST("schedules/")
    suspend fun createSchedule(@Body payload: SchedulePayload): Response<ScheduleResponse>
}
