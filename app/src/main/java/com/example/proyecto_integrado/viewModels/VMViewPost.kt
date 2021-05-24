package com.example.proyecto_integrado.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VMViewPost : ViewModel() {

    private var textReview = MutableLiveData<String>()

    val textReviewLiveData: LiveData<String>
        get() = textReview

    fun setTextReview(s: String){
        textReview.value = s
    }

    private var title = MutableLiveData<String>()

    val titleLiveData: LiveData<String>
        get() = textReview

    fun setTitle(s: String){
        textReview.value = s
    }


    private var urlPhotoGame = MutableLiveData<String>()

    val urlPhotoGameLiveData: LiveData<String>
        get() = urlPhotoGame

    fun setUrlPhotoGame(s: String){
        urlPhotoGame.value = s
    }

}