package com.infos.androidtask.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infos.androidtask.data.TaskData
import com.infos.androidtask.data.remote.Repository
import com.infos.androidtask.util.Resource
import com.infos.androidtask.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskUseCase: TaskUseCase,
    private val tokenManager: TokenManager,
    private val repository: Repository
) : ViewModel() {
    private val _task = MutableLiveData<Resource<List<TaskData>>>()
    val task: LiveData<Resource<List<TaskData>>> = _task

    init {
        authorizeAndFetchTask()
    }

    private fun authorizeAndFetchTask() {
        viewModelScope.launch {
            try {
                // authorize
                val response = repository.authorize()
                val token = response.oauth.access_token
                tokenManager.saveAccessToken(token)

                fetchTask()
            } catch (e: Exception) {
                if (e is HttpException) {
                    e.message?.let {
                        Log.e(Repository::class.simpleName, it)
                    }
                } else {
                    e.message?.let { Log.e(Repository::class.simpleName, it) }
                    e.printStackTrace()
                }
            }
        }
    }

    private fun fetchTask() {
        viewModelScope.launch {
            try {
                taskUseCase().collect { result ->
                    when (result.status) {
                        Resource.Status.SUCCESS -> {
                            _task.value = result
                            result.data?.let { repository.saveData(it) }
                        }
                        Resource.Status.ERROR -> {
                            Log.e(
                                HomeViewModel::class.simpleName,
                                result.message ?: "Unknown error occurred"
                            )
                        }
                        Resource.Status.LOADING -> {
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is HttpException) {
                    e.message?.let {
                        Log.e(Repository::class.simpleName, it)
                    }
                } else {
                    e.message?.let { Log.e(Repository::class.simpleName, it) }
                    e.printStackTrace()
                }
            }
        }
    }

    fun getLocal() : List<TaskData>{
        return  repository.getData()
    }


}
