package com.example.pastillacontrol.data.remote

import android.content.Context
import com.example.pastillacontrol.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BackendClient {
    fun create(context: Context): BackendApi {
        val sessionStore = SessionStore(context)

        val authInterceptor = Interceptor { chain ->
            val token = sessionStore.getToken()
            val request = chain.request().newBuilder().apply {
                if (!token.isNullOrBlank()) {
                    addHeader("Authorization", "Bearer $token")
                }
            }.build()
            chain.proceed(request)
        }

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(BackendApi::class.java)
    }
}
