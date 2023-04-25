package com.infos.androidtask.data.remote

import com.infos.androidtask.data.user.LoginResponse
import com.infos.androidtask.data.user.UserLoginRequest
import com.infos.androidtask.util.Constants.CREDENTIAL_PASSWORD
import com.infos.androidtask.util.Constants.CREDENTIAL_USERNAME
import com.infos.androidtask.util.Constants.GIVEN_AUTH_HEADER
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun authorize(): LoginResponse{
        val body = UserLoginRequest(CREDENTIAL_USERNAME,CREDENTIAL_PASSWORD)
        return apiService.login(GIVEN_AUTH_HEADER,body)
    }
    suspend fun getItems(authToken:String) = apiService.getItems(authToken)
}