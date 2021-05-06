package com.example.proyecto_integrado

class Comentario {

    var comentario: String = ""
        get() {
            return field
        }
        set(nuevoComentario) {
            field = nuevoComentario
        }

    var idUser: String = ""
        get() {
            return field
        }
        set(nuevoComentario) {
            field = nuevoComentario
        }

    var valoracion: Int = 0
        get() {
            return field
        }
        set(nuevoComentario) {
            field = nuevoComentario
        }

    init {
        hashMapOf(
            "comentario" to comentario,
            "idUser" to idUser,
            "valoracion" to valoracion
        )
    }
}