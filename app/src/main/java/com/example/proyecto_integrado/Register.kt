package com.example.proyecto_integrado

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Register : AppCompatActivity() {
    val RC_SIGN_IN = 1664
    private var mauth = Firebase.auth
    private lateinit var googleSignInClient: GoogleSignInClient
    val db = Firebase.firestore
    val storage = Firebase.storage
    var storageRef = storage.reference
    val SELECT_ACTIVITY = 50
    private var imageUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == SELECT_ACTIVITY && resultCode == Activity.RESULT_OK -> {
                imageUri = data!!.data
                imageUri?.let { cargarImagen(it) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register).apply {
            findViewById<ComposeView>(R.id.compose_view).setContent {
                // In Compose world
                MaterialTheme {
                    val context = LocalContext.current

                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()

                    googleSignInClient = GoogleSignIn.getClient(context, gso)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Button(onClick = {
                            //TODO hacer que se carge la imagen


                            ImageController.selectPhotoFromGallery(this@Register, SELECT_ACTIVITY)

                        }) { Text(getString(R.string.upload_potho)) }

                        Spacer(modifier = Modifier.height(16.dp))


                        var error by remember { mutableStateOf(false) }
                        val user = remember { mutableStateOf(TextFieldValue("")) }
                        val email = remember { mutableStateOf(TextFieldValue("")) }
                        val password = remember { mutableStateOf(TextFieldValue("")) }
                        val repeat_password = remember { mutableStateOf(TextFieldValue("")) }


                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            value = user.value,
                            isError = error,
                            onValueChange = {
                                user.value = it
                                error = false
                            },
                            label = { Text(getString(R.string.user)) })
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            value = email.value,
                            isError = error,
                            onValueChange = {
                                email.value = it
                                error = false
                            },
                            label = { Text(getString(R.string.email)) })
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            value = password.value,
                            isError = error,
                            onValueChange = {
                                password.value = it
                                error = false
                            },
                            label = { Text(getString(R.string.password)) })
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            value = repeat_password.value,
                            isError = error,
                            onValueChange = {
                                repeat_password.value = it
                                error = false
                            },
                            label = { Text(getString(R.string.repeat_password)) })
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            if (user.value.text.length != 0 && email.value.text.length != 0 && password.value.text.length != 0 && repeat_password.value.text.length != 0) {
                                if (password.value == repeat_password.value) {
                                    mauth.createUserWithEmailAndPassword(
                                        email.value.text,
                                        password.value.text
                                    )
                                        .addOnCompleteListener(this@Register) { task ->
                                            if (task.isSuccessful) {
                                                // Sign in success, update UI with the signed-in user's information

                                                val userId = mauth.currentUser.uid

                                                var file = imageUri
                                                //Uri.fromFile(File("Internal storage/WhatsApp/Media/Whatsapp Images/IMG-20210413-WA0002.jpg"))
                                                val riversRef = storageRef.child(userId)
                                                var uploadTask = file?.let { riversRef.putFile(it) }

                                                // Register observers to listen for when the download is done or if it fails
                                                if (uploadTask != null) {
                                                    uploadTask.addOnFailureListener {
                                                        // Handle unsuccessful uploads
                                                    }.addOnSuccessListener {
                                                        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                                                        // ...
                                                    }
                                                }

                                                Handler().postDelayed(Runnable {
                                                    val data = hashMapOf(
                                                        "user" to user.value.text,
                                                        "userId" to userId,
                                                        "photo" to userId
                                                    )

                                                    db.collection("users")
                                                        .add(data)
                                                        .addOnSuccessListener { documentReference ->
                                                            Log.d(
                                                                ContentValues.TAG,
                                                                "DocumentSnapshot written with ID: ${documentReference.id}"
                                                            )
                                                        }
                                                        .addOnFailureListener { e ->
                                                            Log.w(
                                                                ContentValues.TAG,
                                                                "Error adding document",
                                                                e
                                                            )
                                                        }
                                                }, 500)


                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Toast.makeText(
                                                    baseContext, R.string.authentication_failed,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                error = true

                                            }
                                        }
                                } else {
                                    Toast.makeText(
                                        baseContext, R.string.wrong_password,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    error = true
                                }
                            } else {
                                Toast.makeText(
                                    baseContext, R.string.empty_data,
                                    Toast.LENGTH_SHORT
                                ).show()
                                error = true
                            }


                        }) { Text(getString(R.string.register)) }
                    }
                }
            }
        }


    }

    fun cargarImagen(url: String?) {
        val image = findViewById<View>(R.id.imageView) as ImageView
        Glide.with(this).load(url).into(image)
    }

    fun cargarImagen(url: Uri) {
        val image = findViewById<View>(R.id.imageView) as ImageView
        Glide.with(this).load(url).into(image)
    }

}