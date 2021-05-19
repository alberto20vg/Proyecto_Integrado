package com.example.proyecto_integrado.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VMCreatePost : ViewModel() {

    private var gameName = MutableLiveData<String>()

        val gameNameLiveData: LiveData<String>
             get() = gameName

    fun setGameName(s: String){
        gameName.value = s
    }


    private var urlPhotoGame = MutableLiveData<String>()

    val urlPhotoGameLiveData: LiveData<String>
        get() = urlPhotoGame

    fun setUrlPhotoGame(s: String){
        urlPhotoGame.value = s
    }

}