package com.da.data.local.db

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        //val res = context.deleteDatabase("app_database")
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build()

            INSTANCE = instance
            instance
        }
    }

    fun clear() {
        INSTANCE?.clearAllTables()
    }
}