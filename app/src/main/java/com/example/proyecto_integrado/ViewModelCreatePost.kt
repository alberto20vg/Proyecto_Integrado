package com.example.proyecto_integrado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelCreatePost : ViewModel() {

    private var gameName = MutableLiveData<String>()

        val gameNameLiveData: LiveData<String>
             get() = gameName

    fun setGameName(s:String){
        gameName.value = s
    }

    val counterLiveData: LiveData<Int>
        get() = counter

    private val counter = MutableLiveData<Int>()
    private var count = 0

    fun increaseCounter() {
        counter.value = ++count
    }
}