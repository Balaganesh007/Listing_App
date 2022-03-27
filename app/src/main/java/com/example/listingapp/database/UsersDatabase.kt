package com.example.listingapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UsersModel::class], version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {
    abstract val userDataDao : UserDataDao
}

private lateinit var INSTANCE: UsersDatabase

fun getDatabase(context: Context): UsersDatabase {
    synchronized(UsersDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                UsersDatabase::class.java,
                "Database").build()
        }
    }
    return INSTANCE
}