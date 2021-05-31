package com.example.proyecto_integrado.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyecto_integrado.R

class VMPostView : ViewModel() {

    var star1 = MutableLiveData<String>()

    val star1LiveData: MutableLiveData<String>
        get() = star1


    fun setStar1(s: String) {
        star1.value = s
    }

    var star2 = MutableLiveData<String>()

    val star2LiveData: MutableLiveData<String>
        get() = star2


    fun setStar2(s: String) {
        star2.value = s
    }

    var star3 = MutableLiveData<String>()

    val star3LiveData: MutableLiveData<String>
        get() = star3


    fun setStar3(s: String) {
        star3.value = s
    }

    var star4 = MutableLiveData<String>()

    val star4LiveData: MutableLiveData<String>
        get() = star4


    fun setStar4(s: String) {
        star4.value = s
    }

    var star5 = MutableLiveData<String>()

    val star5LiveData: MutableLiveData<String>
        get() = star5


    fun setStar5(s: String) {
        star5.value = s
    }

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