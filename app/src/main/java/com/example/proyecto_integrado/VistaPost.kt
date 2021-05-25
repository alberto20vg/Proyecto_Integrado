package com.example.proyecto_integrado

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_integrado.PostsQuerys.Posts
import com.example.proyecto_integrado.ui.theme.Teal200
import com.example.proyecto_integrado.viewModels.VMCreatePost
import com.example.proyecto_integrado.viewModels.VMViewPost
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import dev.chrisbanes.accompanist.coil.CoilImage

private val db = Firebase.firestore
private var mauth = Firebase.auth
private var user = mauth.currentUser

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
    fun vistaPost(valor: String, model: VMViewPost = viewModel()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val docRef = db.collection("posts").document(valor)
            if (docRef != null) {
                docRef
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            model.setUrlPhotoGame(document.getString("urlPhotoJuego").toString())
                            model.setTitle(document.getString("title").toString())
                            model.setTextReview(document.getString("textReview").toString())

                        }
                    }.addOnFailureListener {}
            }

            val docRef2 = db.collection("users").document(user.uid)
            if (docRef2 != null) {
                docRef2
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            model.setStarPosts(document.get("starPosts") as ArrayList<String>)
                        }
                    }.addOnFailureListener {}
            }


            val urlPhotoGame by model.urlPhotoGameLiveData.observeAsState("")
            val textReview by model.textReviewLiveData.observeAsState("")
            val title by model.titleLiveData.observeAsState("")
            val starPhotoPost by model.starPhotoPostLiveData.observeAsState("https://firebasestorage.googleapis.com/v0/b/proyecto-integrado-8b304.appspot.com/o/empty_star.png?alt=media&token=f73c0975-9d40-44ba-9221-5c7f92cf8764")
            val starPosts by model.starPostsLiveData.observeAsState(null)

            CoilImage(
                data = urlPhotoGame,
                contentDescription = "game",
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .height(150.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.background(
                            color = Teal200
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.padding(16.dp))

            CoilImage(
                data = starPhotoPost,
                contentDescription = "game",
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        model.setStarPhotoPost()

                        if (starPhotoPost == "https://firebasestorage.googleapis.com/v0/b/proyecto-integrado-8b304.appspot.com/o/full_star.png?alt=media&token=164420ba-1863-4951-8741-f6582bf8c789") {
                            starPosts?.add(valor)
                            db
                                .collection("users")
                                .document(user.uid)
                                .update("starPosts", starPosts)
                        } else {
                            starPosts?.remove(valor)
                            db
                                .collection("users")
                                .document(user.uid)
                                .update("starPosts", starPosts)
                        }
                    },
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.background(
                            color = Teal200
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Text(
                text = textReview,
                style = MaterialTheme.typography.h5
            )
            //TODO recycler comentarios
            //TODO cosas para postear
        }

    }
}