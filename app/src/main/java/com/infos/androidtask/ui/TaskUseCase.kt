package com.infos.androidtask.ui

import com.infos.androidtask.data.TaskData
import com.infos.androidtask.data.remote.Repository
import com.infos.androidtask.util.Constants
import com.infos.androidtask.util.Resource
import com.infos.androidtask.util.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TaskUseCase @Inject constructor(
    private val repository: Repository,
    private val tokenManager: TokenManager
) {
    operator fun invoke(): Flow<Resource<List<TaskData>>> = flow {
        try {
            emit(Resource.loading())
            val authToken = Constants.AUTH_BEARER + tokenManager.getAccessToken()
            val task = repository.getItems(authToken)
            emit(Resource.success(task))
        }catch (e: HttpException){
            emit(Resource.error(e.localizedMessage))
        }catch (e : IOException){
            emit(Resource.error("Check your internet connect "))
        }
    }

}