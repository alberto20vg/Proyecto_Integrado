package com.example.proyecto_integrado.CommentsPackage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CommentsViewModel(val commentsRepo: CommentsRepo) : ViewModel() {
    val commentsStateFlow = MutableStateFlow<CommentsResponse?>(null)

    init {
        viewModelScope.launch {
            //TODO ver como soluciono esto
            var valor = "ENuk29GTQbeLFoQdhySB"
            commentsRepo.getComments(valor).collect { commentsStateFlow.value = it }
        }
    }

    fun getCommentsInfo(valor: String) = commentsRepo.getComments(valor)
}
