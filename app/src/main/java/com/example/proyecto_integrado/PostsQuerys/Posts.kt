package com.example.proyecto_integrado.PostsQuerys

data class Posts(
    val autor: String,
    val gameName: String,
    val textReview: String,
    val title: String,
    val urlPhotoGame: String,
    val urlPhotoUser: String,
    val userName: String,
    val email:String,
    val postId:String
) {
    constructor() : this("","", "", "", "", "", "", "", "")
}