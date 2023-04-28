package com.infos.androidtask.data


import com.infos.androidtask.data.local.LocalDataSource
import com.infos.androidtask.data.remote.RemoteDataSource
import com.infos.androidtask.data.response.TaskData
import com.infos.androidtask.data.response.user.LoginResponse
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    suspend fun authorize(): LoginResponse {
        return remoteDataSource.authorize()

    }
    suspend fun getItems(authToken:String) = remoteDataSource.getItems(authToken)

    fun getData()= localDataSource.getLocal()

    suspend fun saveData(model : List<TaskData>) = localDataSource.saveData(model)

}