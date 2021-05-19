package com.example.proyecto_integrado

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.proyecto_integrado.ui.theme.Teal200
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev.chrisbanes.accompanist.coil.CoilImage

private const val SELECT_ACTIVITY = 50
private var imageUri: Uri? = null
private val storage = Firebase.storage
private var storageRef = storage.reference
private val db = Firebase.firestore

class AddGame : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game).apply {
            findViewById<ComposeView>(R.id.compose_view).setContent { MaterialTheme { addGame() } }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == SELECT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imageUri = data!!.data
                imageUri?.let { uploadImage(it) }
            }
        }
    }

    @Composable
    fun addGame() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight().padding(top = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                ImageController.selectPhotoFromGallery(this@AddGame, SELECT_ACTIVITY)
            }) { Text(getString(R.string.upload_potho)) }

            Spacer(modifier = Modifier.height(16.dp))

            var title by remember {
                mutableStateOf("")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(getString(R.string.title)) },
                maxLines = 2,
                textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(20.dp)
                    .height(50.dp)
            )

            Button(onClick = {
                val file = imageUri
                val imageRef = storageRef.child(title.replace(" ", ""))
                val uploadTask = file?.let { imageRef.putFile(it) }

                uploadTask?.addOnFailureListener {
                }?.addOnSuccessListener {
                }

                val data = hashMapOf(
                    "urlGame" to title.replace(" ", ""),
                    "gameName" to title
                )

                db.collection("games").document(title.replace(" ", "")).set(data)

                val intent = Intent(this@AddGame, CreatePost::class.java)
                startActivity(intent)
                finish()

            }) {
                Text(
                    getString(R.string.add_game)
                )
            }
        }
    }

    private fun uploadImage(url: Uri) {
        val image = findViewById<View>(R.id.imageView) as ImageView
        Glide.with(this).load(url).into(image)
    }

    private fun uploadImage(url: Int) {
        val image = findViewById<View>(R.id.imageView) as ImageView
        Glide.with(this).load(url).apply(RequestOptions.circleCropTransform()).into(image)
    }
}

