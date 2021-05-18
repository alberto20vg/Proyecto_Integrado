package com.example.proyecto_integrado.PostsQuerys

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PostsViewModel(val postsRepo: PostsRepo) : ViewModel() {
    val postsStateFlow = MutableStateFlow<PostsResponse?>(null)

    init {
        viewModelScope.launch {
            postsRepo.getHome().collect {
                postsStateFlow.value = it
            }

            postsRepo.getMyPosts().collect {
                postsStateFlow.value = it
            }

        }
    }

    fun getPostInfo() = postsRepo.getHome()
    fun getMyPostInfo() = postsRepo.getMyPosts()
}