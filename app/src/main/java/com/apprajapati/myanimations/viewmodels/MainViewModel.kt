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
import java.lang.Exception
import java.util.Timer
import java.util.TimerTask

class MainViewModel : ViewModel() {

    private val _time: MutableLiveData<NetworkResult<Time>> = MutableLiveData()
    val time: LiveData<NetworkResult<Time>> get() = _time

    private val _timeApiTime: MutableLiveData<NetworkResult<TimeApiTime>> = MutableLiveData()
    val timeApi: LiveData<NetworkResult<TimeApiTime>> get() = _timeApiTime

    private var timer = Timer()

    fun getTime() {
        viewModelScope.launch {
            try {
                val gotTime = RetrofitInstance.api.getTime()
                _time.value = handleResponse(gotTime)
            }catch (e: Exception){
                e.printStackTrace() //Occasions where internet connection will be lost so api.getTime() will be kept calling and failing so you must handle this exception and stop the thread.
                _time.value = NetworkResult.Error("Error fetching time!")
                stopTimer()
            }
        }
    }

    fun getTimeFromTimeApi() {
        viewModelScope.launch {
            val response = RetrofitInstance.api.getTimeTimeApi()
            _timeApiTime.value = handleResponseTimeApi(response)
        }
    }

    fun startFetchingTime() {
        val task = object : TimerTask() {
            override fun run() {
                getTime()
            }

        }
        timer = Timer()
        timer.scheduleAtFixedRate(task, 0, 1000)
        //delay = start the task after that much time has passed.
        //period = run timer every 1 second, 1000 milliseconds=1 second.
    }

    fun stopTimer() {
        timer.cancel()
        timer.purge()
    }

    private fun handleResponse(response: Response<Time>): NetworkResult<Time> {
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

    private fun handleResponseTimeApi(response: Response<TimeApiTime>): NetworkResult<TimeApiTime> {
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