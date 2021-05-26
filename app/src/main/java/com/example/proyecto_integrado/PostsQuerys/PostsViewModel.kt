package com.example.proyecto_integrado.PostsQuerys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
            postsRepo.getStarPosts(prueba()).collect {
                postsStateFlow.value = it
            }

        }
    }

    fun getPostInfo() = postsRepo.getHome()
    fun getMyPostInfo() = postsRepo.getMyPosts()


    fun getStarPosts() = postsRepo.getStarPosts(prueba())

//TODO es un mamon y no me pilla lo del docRef2
//    y los sharedPreference no funcionan
    fun prueba(): ArrayList<String> {
        val arrayList = arrayListOf("ENuk29GTQbeLFoQdhySB")
        val docRef2 = db.collection("users").document(user.uid)

            docRef2
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val arrayList2 = document.get("starPosts") as ArrayList<String>
                        for (i in arrayList2) {
                            arrayList.add(i)
                        }
                  //      return@addOnSuccessListener arrayList as Unit
                    }
                }.addOnFailureListener {}
        return arrayList
    }

}