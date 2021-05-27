package com.example.proyecto_integrado.PostsQuerys

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.internal.util.HalfSerializer.onComplete
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.ArrayList


class PostsViewModel(val postsRepo: PostsRepo) : ViewModel() {

    private val db = Firebase.firestore
    private var mauth = Firebase.auth
    private var user = mauth.currentUser

    val postsStateFlow = MutableStateFlow<PostsResponse?>(null)

    init {
        viewModelScope.launch {
            postsRepo.getHome().collect {
                postsStateFlow.value = it
            }

            postsRepo.getMyPosts().collect {
                postsStateFlow.value = it
            }
            postsRepo.getStarPosts(queryStarPosts()).collect {
                postsStateFlow.value = it
            }
        }
    }

    fun getPostInfo() = postsRepo.getHome()
    fun getMyPostInfo() = postsRepo.getMyPosts()
    fun getStarPosts() = postsRepo.getStarPosts(queryStarPosts())

    fun queryStarPosts(): ArrayList<String> {
        val arrayList = arrayListOf("")
        val docRef2 = db.collection("users").document(user.uid)

        docRef2
            .get().addOnCompleteListener { document ->
                val arrayList2 =  document.result?.get("starPosts") as ArrayList<String>
                for (i in arrayList2) {
                    arrayList.add(i)
                }
            }
        /*
    .addOnSuccessListener{ document ->
        if (document != null) {
            val arrayList2 = document.get("starPosts") as ArrayList<String>
            for (i in arrayList2) {
                arrayList.add(i)
            }
      //      return@addOnSuccessListener arrayList as Unit
        }
    }.addOnFailureListener {}

         */
        Thread.sleep(200)
        return arrayList
    }

}