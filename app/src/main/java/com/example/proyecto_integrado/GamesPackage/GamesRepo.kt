package com.example.proyecto_integrado.GamesPackage

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class GamesRepo {
    private val firestore = FirebaseFirestore.getInstance()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getGames() = callbackFlow {
        val collection = firestore.collection("games")
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