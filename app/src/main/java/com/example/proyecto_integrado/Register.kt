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
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
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

class Register : ComponentActivity() {
    private var mauth = Firebase.auth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private var storageRef = storage.reference
    private val SELECT_ACTIVITY = 50
    private var imageUri: Uri? = null

    //recojo la uri de la imagen
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
                            ImageController.selectPhotoFromGallery(this@Register, SELECT_ACTIVITY)
                        }) { Text(getString(R.string.upload_potho)) }

                        Spacer(modifier = Modifier.height(16.dp))

                        var error by remember { mutableStateOf(false) }
                        val user = remember { mutableStateOf(TextFieldValue("")) }
                        val email = remember { mutableStateOf(TextFieldValue("")) }
                        val password = remember { mutableStateOf(TextFieldValue("")) }
                        val repeatPassword = remember { mutableStateOf(TextFieldValue("")) }

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
                            value = repeatPassword.value,
                            isError = error,
                            onValueChange = {
                                repeatPassword.value = it
                                error = false
                            },
                            label = { Text(getString(R.string.repeat_password)) })

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            var isUserEmpty = user.value.text.isEmpty()
                            var isEmailEmpty = email.value.text.isEmpty()
                            var isPasswordEmpty = password.value.text.isEmpty()
                            var isRepeatPasswordEmpty = repeatPassword.value.text.isEmpty()
                            var passwordsMatch = password.value == repeatPassword.value

                            if (isUserEmpty || isEmailEmpty || isPasswordEmpty || isRepeatPasswordEmpty) {
                                Toast.makeText(
                                    baseContext, R.string.authentication_failed,
                                    Toast.LENGTH_SHORT
                                ).show()
                                error = true
                            } else {
                                if (passwordsMatch) {
                                    mauth.createUserWithEmailAndPassword(
                                        email.value.text,
                                        password.value.text
                                    ).addOnCompleteListener(this@Register) { task ->
                                        if (task.isSuccessful) {
                                            val userId = mauth.currentUser.uid
                                            var file = imageUri
                                            val riversRef = storageRef.child(userId)
                                            var uploadTask = file?.let { riversRef.putFile(it) }

                                            uploadTask?.addOnFailureListener {
                                            }?.addOnSuccessListener {
                                            }

                                            Handler().postDelayed({
                                                val data = hashMapOf(
                                                    "user" to user.value.text,
                                                    "userId" to userId,
                                                    "photo" to userId
                                                )

                                                db.collection("users").document(userId).set(data)
                                            }, 500)

                                            //TODO revisar que aqui funcione bien el intent
                                            val intent = Intent(context, NavBar::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        baseContext, R.string.wrong_password,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    error = true
                                }
                            }
                        }) { Text(getString(R.string.register)) }
                    }
                }
            }
        }
    }

    private fun cargarImagen(url: Uri) {
        val image = findViewById<View>(R.id.imageView) as ImageView
        Glide.with(this).load(url).into(image)
    }

}


