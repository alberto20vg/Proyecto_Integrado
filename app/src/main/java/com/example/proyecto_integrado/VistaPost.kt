package com.example.proyecto_integrado

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

private val db = Firebase.firestore
private  var titulo: String = ""
private  var urlPhoto: String=""
private  var  textoresena: String=""

var prueba = ArrayList<Comentario>()
val data = hashMapOf(
    "user" to "user1",
    "userId" to "userid1",
    "photo" to "photo1"
)

class VistaPost : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val objetoIntent: Intent = intent
        val valor = objetoIntent.getStringExtra("idPost")

        val docRef = valor?.let { db.collection("posts").document(it) }
        if (docRef != null) {
            docRef
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        titulo = document.getString("titulo").toString()
                        urlPhoto = document.getString("urlPhotoJuego").toString()
                        textoresena = document.getString("textoResena").toString()
                        //TODO me saca el mapa pero da problema si quiero sacar los datos concretos
                        prueba = document.get("comentarios") as ArrayList<Comentario>

                    }
                }.addOnFailureListener {}
        }
        setContent {
            var handler = Handler()
            handler.postDelayed(
                {
                    Toast.makeText(
                        this, prueba.toString()
                        //[0].comentario
                        , Toast.LENGTH_SHORT
                    ).show()
                }, 1000
            )

            vistaPost()
        }
    }

    @Composable
    fun vistaPost() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.h5
            )
            //TODO estrella favorito
            var imagen = R.drawable.empty_star
            Image(
                painter = painterResource(id = imagen),
                contentDescription = "Localized description",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .clickable(
                        enabled = true,
                        onClickLabel = "Clickable image",
                        onClick = {
                            imagen = R.drawable.full_star
                            Toast
                                .makeText(
                                    this@VistaPost,
                                    "Image clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                    )
            )
            Text(
                text = textoresena,
                style = MaterialTheme.typography.h5
            )
            //TODO recycler comentarios
            //TODO cosas para postear
        }

    }
}