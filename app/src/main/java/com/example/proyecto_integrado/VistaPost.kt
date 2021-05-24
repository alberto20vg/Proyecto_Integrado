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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_integrado.PostsQuerys.Posts
import com.example.proyecto_integrado.viewModels.VMCreatePost
import com.example.proyecto_integrado.viewModels.VMViewPost
import com.google.firebase.firestore.ktx.toObject

private val db = Firebase.firestore
private  var titulo: String = ""
private  var urlPhoto: String=""
private  var  textoresena: String=""


val data = hashMapOf(
    "user" to "user1",
    "userId" to "userid1",
    "photo" to "photo1"
)

class VistaPost : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val objetoIntent: Intent = intent
        var valor = objetoIntent.getStringExtra("idPost").toString()

        setContent {
            vistaPost(valor)
        }
    }



    @Composable
    fun vistaPost(valor:String,model: VMViewPost = viewModel()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

//Funciona pero
            val docRef = db.collection("posts").document("eOHX99klrHbPwjblDw1I")
            if (docRef != null) {
                docRef
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                          //  titulo = document.getString("titulo").toString()
                    //        urlPhoto = document.getString("urlPhotoJuego").toString()
                        //    textoresena = document.getString("textoResena").toString()
                            model.setUrlPhotoGame(document.getString("urlPhotoJuego").toString())
                            model.setTitle(document.getString("title").toString())
                            model.setTextReview(document.getString("textReview").toString())
                        }
                    }.addOnFailureListener {}
            }


            val urlPhotoGame by model.urlPhotoGameLiveData.observeAsState("")
            val textReview by model.textReviewLiveData.observeAsState("")
            val title by model.titleLiveData.observeAsState("")
                Text(
                    text = title,
                    style = MaterialTheme.typography.h5
                )

            //TODO estrella favorito
            var imagen = R.drawable.empty_star
            Image(
                painter = painterResource(id = imagen),
                contentDescription = "Localized description",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(10.dp)
                    .height(100.dp)
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
                                )
                                .show()
                        }
                    )
            )
            Text(
                text = textReview,
                style = MaterialTheme.typography.h5
            )
            //TODO recycler comentarios
            //TODO cosas para postear
        }

    }
}