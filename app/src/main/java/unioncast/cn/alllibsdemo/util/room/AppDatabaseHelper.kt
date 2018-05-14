package com.github.jokar.zhihudaily.room

import android.arch.persistence.room.Room
import android.content.Context

/**
 * Created by JokAr on 2017/6/30.
 */
class AppDatabaseHelper constructor(context: Context) {

    val appDataBase = Room.databaseBuilder(context, AppDatabase::class.java,
            "daily").build()

    companion object {
        @Volatile
        var INSTANCE: AppDatabaseHelper? = null

        fun getInstance(context: Context): AppDatabaseHelper {
            if (INSTANCE == null) {
                synchronized(AppDatabaseHelper::class) {
                    if (INSTANCE == null) {
                        INSTANCE = AppDatabaseHelper(context.applicationContext)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}