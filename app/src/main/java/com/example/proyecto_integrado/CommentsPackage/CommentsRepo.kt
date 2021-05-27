package com.example.proyecto_integrado.CommentsPackage

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


class CommentsRepo {
    private val firestore = FirebaseFirestore.getInstance()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getComments(valor: String) = callbackFlow {
        val collection = firestore.collection("comentariosPost").document(valor + "Comments")
            .collection("coments")
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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getComments(valor: MutableLiveData<String>) = callbackFlow {
        val collection =
            firestore.collection("comentariosPost").document(valor.toString() + "Comments")
                .collection("coments")
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