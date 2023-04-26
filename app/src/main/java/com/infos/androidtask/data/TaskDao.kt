package com.infos.androidtask.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {

    @Query("SELECT * FROM taskModel")
    fun getLocal():List<TaskData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDao(model:List<TaskData>)
}