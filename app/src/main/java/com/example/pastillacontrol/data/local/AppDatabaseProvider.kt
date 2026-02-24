package com.example.pastillacontrol.data.local

import android.content.Context
import androidx.room.Room

object AppDatabaseProvider {
    @Volatile
    private var instance: AppDatabase? = null

    fun get(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "pastilla_control.db"
            ).fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
        }
    }
}
