package com.infos.androidtask.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [TaskData::class], version = 1)
abstract class RoomDB : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}