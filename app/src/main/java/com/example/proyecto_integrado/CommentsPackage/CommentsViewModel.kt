package com.example.proyecto_integrado.CommentsPackage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CommentsViewModel(val commentsRepo: CommentsRepo) : ViewModel() {

    val commentsStateFlow = MutableStateFlow<CommentsResponse?>(null)

    val idPostLiveData: LiveData<String>
        get() = idPost

    private var idPost = MutableLiveData<String>()

    fun setIdPost(s: String){
        idPost.value = s
    }

    init {
        viewModelScope.launch {
            commentsRepo.getComments(idPost).collect { commentsStateFlow.value = it }
        }
    }

    fun getCommentsInfo(valor: String) = commentsRepo.getComments(valor)
}
