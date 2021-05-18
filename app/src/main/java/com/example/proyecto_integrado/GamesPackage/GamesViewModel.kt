package com.example.proyecto_integrado.GamesPackage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class GamesViewModel(val gamesRepo: GamesRepo): ViewModel() {
    val gamesStateFlow = MutableStateFlow<GamesResponse?>(null)

    init{
        viewModelScope.launch {
            gamesRepo.getGames().collect {
                gamesStateFlow.value = it
            }
        }
    }
    fun getGamesInfo() = gamesRepo.getGames()
}
