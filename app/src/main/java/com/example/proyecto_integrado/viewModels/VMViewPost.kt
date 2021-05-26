package com.example.proyecto_integrado.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyecto_integrado.R

class VMViewPost : ViewModel() {

    var starPosts = MutableLiveData<ArrayList<String>>()

    val starPostsLiveData: MutableLiveData<ArrayList<String>>
        get() = starPosts


    fun setStarPosts(s: ArrayList<String>) {
        starPosts.value = s
    }

    private var textReview = MutableLiveData<String>()

    val textReviewLiveData: LiveData<String>
        get() = textReview

    fun setTextReview(s: String) {
        textReview.value = s
    }

    private var title = MutableLiveData<String>()

    val titleLiveData: LiveData<String>
        get() = title

    fun setTitle(s: String) {
        title.value = s
    }


    private var urlPhotoGame = MutableLiveData<String>()

    val urlPhotoGameLiveData: LiveData<String>
        get() = urlPhotoGame

    fun setUrlPhotoGame(s: String) {
        urlPhotoGame.value = s
    }

    private var starPhotoPost = MutableLiveData<String>()

    val starPhotoPostLiveData: LiveData<String>
        get() = starPhotoPost

    fun setStarPhotoPost() {
        if (starPhotoPost.value == "https://firebasestorage.googleapis.com/v0/b/proyecto-integrado-8b304.appspot.com/o/full_star.png?alt=media&token=164420ba-1863-4951-8741-f6582bf8c789") {
            starPhotoPost.value =
                "https://firebasestorage.googleapis.com/v0/b/proyecto-integrado-8b304.appspot.com/o/empty_star.png?alt=media&token=f73c0975-9d40-44ba-9221-5c7f92cf8764"
        } else {
            starPhotoPost.value =
                "https://firebasestorage.googleapis.com/v0/b/proyecto-integrado-8b304.appspot.com/o/full_star.png?alt=media&token=164420ba-1863-4951-8741-f6582bf8c789"
        }
    }

    fun setStarPhotoPost(s: String) {
        starPhotoPost.value = s
    }
}