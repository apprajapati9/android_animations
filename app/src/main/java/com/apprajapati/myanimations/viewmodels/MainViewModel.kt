package com.apprajapati.myanimations.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apprajapati.myanimations.data.network.RetrofitInstance
import com.apprajapati.myanimations.models.Time
import com.apprajapati.myanimations.models.TimeApiTime
import com.apprajapati.myanimations.util.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _time : MutableLiveData<NetworkResult<Time>> = MutableLiveData()
    val time : LiveData<NetworkResult<Time>> get() = _time

    private val _timeApiTime : MutableLiveData<NetworkResult<TimeApiTime>> = MutableLiveData()
    val timeApi : LiveData<NetworkResult<TimeApiTime>> get() = _timeApiTime

    fun getTime(){
        viewModelScope.launch {
            val gotTime = RetrofitInstance.api.getTime()
            _time.value = handleResponse(gotTime)
        }
    }

    fun getTimeFromTimeApi(){
        viewModelScope.launch {
            val response = RetrofitInstance.api.getTimeTimeApi()
            _timeApiTime.value = handleResponseTimeApi(response)
        }
    }

    private fun handleResponse(response: Response<Time>) : NetworkResult<Time> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited!")
            }
            response.code() == 429 -> {
                return NetworkResult.Error("Too Many Requests!")
            }
            response.isSuccessful -> {
                val Time = response.body()
                return NetworkResult.Success(Time!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }

    }

    private fun handleResponseTimeApi(response: Response<TimeApiTime>) : NetworkResult<TimeApiTime> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited!")
            }
            response.code() == 429 -> {
                return NetworkResult.Error("Too Many Requests!")
            }
            response.isSuccessful -> {
                val Time = response.body()
                return NetworkResult.Success(Time!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }

    }
}