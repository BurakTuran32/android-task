package com.infos.androidtask.data.remote

import com.infos.androidtask.data.response.TaskData
import com.infos.androidtask.data.response.user.LoginResponse
import com.infos.androidtask.data.request.UserLoginRequest
import com.infos.androidtask.util.Constants
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST(Constants.LOGIN_URL)
    suspend fun login(
        @Header("Authorization") authHeader: String,
        @Body request: UserLoginRequest
    ): LoginResponse

    @GET(Constants.RESOURCES_URL)
    suspend fun getItems(@Header("Authorization") token: String): List<TaskData>
}