package com.infos.androidtask.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskData::class], version = 1)
abstract class TaskDB : RoomDatabase() {

    abstract fun taskDao() : TaskDao


    companion object{

        @Volatile private var instance : TaskDB? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,TaskDB::class.java,"taskdatabase"
        ).build()

    }
}