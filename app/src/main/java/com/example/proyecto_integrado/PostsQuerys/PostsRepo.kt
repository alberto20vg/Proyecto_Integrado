package com.example.proyecto_integrado.PostsQuerys


import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class PostsRepo {
    private val firestore = FirebaseFirestore.getInstance()
    private var mauth = Firebase.auth
    private var user = mauth.currentUser

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getHome() = callbackFlow {
        val collection = firestore.collection("posts")
        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                OnSuccess(value)
            } else {
                OnError(error)
            }
            offer(response)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }


    fun getMyPosts() = callbackFlow {
        val collection = firestore.collection("posts").whereEqualTo("email",user.email)
        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                OnSuccess(value)
            } else {
                OnError(error)
            }
            offer(response)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }
//TODO hacer query cuando este hecha la lista de favoritos
    fun getStarPosts() = callbackFlow {
        val collection = firestore.collection("posts")
        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                OnSuccess(value)
            } else {
                OnError(error)
            }
            offer(response)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }
}