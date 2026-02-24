package com.example.pastillacontrol.core.localization

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object AppLanguageManager {
    private const val PREFS = "app_language"
    private const val KEY_LANGUAGE_TAG = "language_tag"
    const val LANGUAGE_ENGLISH = "en"
    const val LANGUAGE_SPANISH = "es"

    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE_TAG, LANGUAGE_ENGLISH).orEmpty().ifBlank {
            LANGUAGE_ENGLISH
        }
    }

    fun applySavedLanguage(context: Context) {
        val languageTag = getSavedLanguage(context)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag))
    }

    fun setLanguage(context: Context, languageTag: String) {
        val normalized = when (languageTag) {
            LANGUAGE_SPANISH -> LANGUAGE_SPANISH
            else -> LANGUAGE_ENGLISH
        }

        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE_TAG, normalized).apply()

        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(normalized))
    }
}
