package com.example.pastillacontrol.data.remote

import android.content.Context
import java.util.UUID

class SessionStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getOrCreateDemoEmail(): String {
        val existing = prefs.getString(KEY_EMAIL, null)
        if (!existing.isNullOrBlank()) return existing
        val generated = "pastilla_${UUID.randomUUID().toString().take(8)}@example.com"
        prefs.edit().putString(KEY_EMAIL, generated).apply()
        return generated
    }

    fun getOrCreateDemoPassword(): String {
        val existing = prefs.getString(KEY_PASSWORD, null)
        if (!existing.isNullOrBlank()) return existing
        val generated = "DemoPass!123"
        prefs.edit().putString(KEY_PASSWORD, generated).apply()
        return generated
    }

    companion object {
        private const val PREFS = "remote_session"
        private const val KEY_TOKEN = "token"
        private const val KEY_EMAIL = "demo_email"
        private const val KEY_PASSWORD = "demo_password"
    }
}
