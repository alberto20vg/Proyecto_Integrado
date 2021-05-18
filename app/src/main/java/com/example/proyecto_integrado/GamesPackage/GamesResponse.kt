package com.example.proyecto_integrado.GamesPackage

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


sealed class GamesResponse
data class OnSuccess(val querySnapshot: QuerySnapshot?) : GamesResponse()
data class OnError(val exception: FirebaseFirestoreException?) : GamesResponse()