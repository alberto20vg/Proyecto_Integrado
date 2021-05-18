package com.example.proyecto_integrado.PostsQuerys

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

sealed class PostsResponse
data class OnSuccess(val querySnapshot: QuerySnapshot?): PostsResponse()
data class OnError(val exception: FirebaseFirestoreException?): PostsResponse()
