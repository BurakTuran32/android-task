package com.infos.androidtask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.infos.androidtask.data.response.TaskData


@Database(entities = [TaskData::class], version = 1)
abstract class RoomDB : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}