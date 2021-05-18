package com.example.proyecto_integrado.PostsQuerys

data class Posts(
    val autor: String,
    val comentarios: String,
    val gameName: String,
    val textReview: String,
    val title: String,
    val urlPhotoJuego: String,
    val urlPhotoUser: String,
    val userName: String
) {
    constructor() : this("", "", "", "", "", "", "", "")
}