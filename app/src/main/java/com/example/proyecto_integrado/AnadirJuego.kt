package com.example.proyecto_integrado

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyecto_integrado.ui.theme.Teal200
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev.chrisbanes.accompanist.coil.CoilImage

private val SELECT_ACTIVITY = 50
private var imageUri: Uri? = null
private val storage = Firebase.storage
private var storageRef = storage.reference
private val db = Firebase.firestore

class AnadirJuego : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            anadirJuego()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == SELECT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imageUri = data!!.data

              //  imageUri?.let { cargarImagen(it) }
            }
        }
    }

    @Composable
    fun anadirJuego() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //TODO la imagen se carga cuando modifico el texto
            imageUri?.let {
                CoilImage(
                    data = it,
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                ImageController.selectPhotoFromGallery(this@AnadirJuego, SELECT_ACTIVITY)
            }) { Text(getString(R.string.upload_potho)) }

            Spacer(modifier = Modifier.height(16.dp))

            var titulo by remember {
                mutableStateOf("")
            }

            Spacer(modifier = Modifier.height(16.dp))

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

            Button(onClick = {
                var file = imageUri
                val riversRef = storageRef.child(titulo.replace(" ", ""))
                var uploadTask = file?.let { riversRef.putFile(it) }

                uploadTask?.addOnFailureListener {
                }?.addOnSuccessListener {
                }

                val data = hashMapOf(
                    "url" to titulo.replace(" ", ""),
                    "nombreJuego" to titulo
                )

                db.collection("juegos").document(titulo.replace(" ", "")).set(data)

                //TODO intent de vuelta
            }) { Text("Añadir Juego"
               // getString(R.string.upload_potho)
            ) }

        }
    }
}