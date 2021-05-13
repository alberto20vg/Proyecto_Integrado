package com.example.proyecto_integrado.Components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BooksViewModel(val booksRepo: BooksRepo): ViewModel() {

    val booksStateFlow = MutableStateFlow<BooksResponse?>(null)

    init {
        viewModelScope.launch {
            booksRepo.getBookDetails().collect {
                booksStateFlow.value = it
            }
        }
    }

    fun getBooksInfo() = booksRepo.getBookDetails()
}