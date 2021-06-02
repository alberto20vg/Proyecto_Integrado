package com.example.proyecto_integrado.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proyecto_integrado.R

class VMPostView : ViewModel() {

    var urlPhoto = MutableLiveData<String>()

    val urlPhotoLiveData: MutableLiveData<String>
        get() = urlPhoto


    fun setUrlPhoto(s: String) {
        urlPhoto.value = s
    }

    var text = MutableLiveData<String>()

    val textLiveData: MutableLiveData<String>
        get() = text


    fun setText(s: String) {
        text.value = s
    }


    var score = MutableLiveData<String>()

    val scoreLiveData: MutableLiveData<String>
        get() = score


    fun setScore(i: String) {
        score.value = i
    }

    var star1 = MutableLiveData<Int>()

    val star1LiveData: MutableLiveData<Int>
        get() = star1


    fun setStar1(s: Int) {
        star1.value = s
    }

    var star2 = MutableLiveData<Int>()

    val star2LiveData: MutableLiveData<Int>
        get() = star2


    fun setStar2(s: Int) {
        star2.value = s
    }

    var star3 = MutableLiveData<Int>()

    val star3LiveData: MutableLiveData<Int>
        get() = star3


    fun setStar3(s: Int) {
        star3.value = s
    }

    var star4 = MutableLiveData<Int>()

    val star4LiveData: MutableLiveData<Int>
        get() = star4


    fun setStar4(s: Int) {
        star4.value = s
    }

    var star5 = MutableLiveData<Int>()

    val star5LiveData: MutableLiveData<Int>
        get() = star5


    fun setStar5(s: Int) {
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