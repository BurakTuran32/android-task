package com.infos.androidtask.di

import android.content.Context
import androidx.room.Room
import com.infos.androidtask.data.LocalDataSource
import com.infos.androidtask.data.RoomDB
import com.infos.androidtask.data.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun localDataSource(taskDao: TaskDao): LocalDataSource {
        return LocalDataSource(taskDao)
    }

    @Provides
    fun provideRoomDB(@ApplicationContext context: Context): RoomDB {
        return Room
            .databaseBuilder(context, RoomDB::class.java, "LocalDB")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTaskDao(roomDB: RoomDB): TaskDao {
        return roomDB.taskDao()
    }
}