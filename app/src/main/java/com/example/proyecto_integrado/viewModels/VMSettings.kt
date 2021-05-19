package com.example.proyecto_integrado.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VMSettings:ViewModel() {


    private var urlPhoto2 = MutableLiveData<String>()

    val urlPhoto2LiveData: LiveData<String>
        get() = urlPhoto2

    fun setUrlPhoto2(s: String){
        urlPhoto2.value = s
    }

    private var userName = MutableLiveData<String>()

    val userNameLiveData: LiveData<String>
        get() = userName

    fun setUserName(s: String){
       userName.value = s
    }
}