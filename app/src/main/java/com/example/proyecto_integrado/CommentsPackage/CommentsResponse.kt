package com.example.proyecto_integrado.CommentsPackage

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

sealed class CommentsResponse
data class OnSuccess(val querySnapshot: QuerySnapshot?) : CommentsResponse()
data class OnError(val exception: FirebaseFirestoreException?) : CommentsResponse()
