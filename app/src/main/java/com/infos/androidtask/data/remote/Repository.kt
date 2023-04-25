package com.infos.androidtask.data.remote


import com.infos.androidtask.data.user.LoginResponse
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    suspend fun authorize(): LoginResponse {
        return remoteDataSource.authorize()

    }
    suspend fun getItems(authToken:String) = remoteDataSource.getItems(authToken)



}