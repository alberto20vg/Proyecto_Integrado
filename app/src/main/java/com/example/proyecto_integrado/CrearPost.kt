package com.example.proyecto_integrado

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.example.proyecto_integrado.ui.theme.Teal200
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev.chrisbanes.accompanist.coil.CoilImage

private var mauth = Firebase.auth
private lateinit var googleSignInClient: GoogleSignInClient
private val db = Firebase.firestore
private var user = mauth.currentUser
private val storageRef = Firebase.storage.reference
private var urlPhoto = ""
private var nombreUsuario = ""

class CrearPost : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            crearPost()

            storageRef.child(user.uid).downloadUrl.addOnSuccessListener {
                // Got the download URL for 'users/me/profile.png'
                urlPhoto = it.toString()
            }.addOnFailureListener {
            }

            val docRef = db.collection("users").document(user.uid)
            docRef
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        nombreUsuario = document.getString("user").toString()
                    }
                }.addOnFailureListener {}
        }
    }

    @Composable
    fun crearPost() {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //TODO imagen pillada de storage
            CoilImage(
                data = "url de la foto del juego seleccionado",
                contentDescription = "android",
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .padding(16.dp)
                    .height(100.dp)
                    .width(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.background(
                            shape = CircleShape,
                            color = Teal200
                        )
                    )
                },
                error = {
                    Box(
                        modifier = Modifier.background(
                            shape = CircleShape,
                            color = Teal200
                        )
                    )
                }
            )

            var titulo by remember {
                mutableStateOf("Hola")
            }
            var texto by remember {
                mutableStateOf("Hola")
            }
            //TODO lista de los juegos que hay (al hacer click se cambiara la foto)

            TextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Titulo") },
                maxLines = 2,
                textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(20.dp)
                    .height(50.dp)
            )
            //TODO conseguir un campo mas grande para que se pueda ver todo el texto
            TextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Enter text") },
                maxLines = 2,
                textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(20.dp)
                    .height(50.dp)
            )

            if (user.providerData[1].providerId == "google.com") {
                urlPhoto = user.photoUrl.toString()
            } else {
                urlPhoto
            }

            if (user.providerData[1].providerId == "google.com") {
                nombreUsuario = user.displayName
            } else {
                nombreUsuario
            }

            Button(onClick = {
                val data = hashMapOf(
                    "textoResena" to texto,
                    "titulo" to titulo,
                    "urlPhotoUser" to urlPhoto,
                    "userName" to nombreUsuario,
                    //TODO estos dos campos se cambiaran cuando el recycler de juegos este hecho
                    "urlPhotoJuego" to "GodOfWar.jpeg",
                    "nombreJuego" to "God Of War"
                )

                db.collection("posts").add(data)

                //TODO intent de vuelta
            })
            {
                Text(
                    "Postear"
                    // getString(R.string.register)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                //TODO hacer intent para añadir un nuevo juego

                val intent = Intent(this@CrearPost, AnadirJuego::class.java)
                startActivity(intent)
            })
            {
                Text(
                    "Añadir juego"
                    //  getString(R.string.register)
                )
            }
        }


    }
}