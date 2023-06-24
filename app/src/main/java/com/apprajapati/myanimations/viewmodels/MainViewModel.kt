package com.apprajapati.myanimations.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apprajapati.myanimations.data.network.RetrofitInstance
import com.apprajapati.myanimations.models.Time
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _time : MutableLiveData<Time> = MutableLiveData()
    val time : LiveData<Time> get() = _time

    fun getTime(){
        viewModelScope.launch {
            val gotTime = RetrofitInstance.api.getTime()
            _time.value = gotTime
        }
    }
}