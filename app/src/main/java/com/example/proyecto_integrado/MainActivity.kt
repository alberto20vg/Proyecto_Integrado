package com.example.proyecto_integrado

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.proyecto_integrado.ui.theme.Proyecto_IntegradoTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {
    //variables necesarias para firebase
    val RC_SIGN_IN = 1664
    private var mauth = Firebase.auth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //variables necesarias para firebase
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(this, gso)

            Proyecto_IntegradoTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    login()
                }
            }
        }
    }

    //compruebo al principio si esta logeado
    override fun onStart() {
        super.onStart()
        val currentUser = mauth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@MainActivity, NavBar::class.java)
            startActivity(intent)
            finish()
        }
    }

    @Composable
    fun login() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //TODO poner icono de la aplicacion
            Image(painterResource(R.drawable.ic_launcher_foreground), contentDescription = null)

            val email = remember { mutableStateOf(TextFieldValue("")) }
            val contrasena = remember { mutableStateOf(TextFieldValue("")) }
            var error by remember { mutableStateOf(false) }

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

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = contrasena.value,
                isError = error,
                onValueChange = {
                    contrasena.value = it
                    error = false
                },
                label = { Text(getString(R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                var emailEmpty = email.value.text.length == 0
                var passwordEmpty = contrasena.value.text.length == 0

                if (emailEmpty || passwordEmpty) {
                    Toast.makeText(
                        baseContext,
                        getString(R.string.authentication_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                    error = true
                } else {
                    mauth.signInWithEmailAndPassword(email.value.text, contrasena.value.text)
                        .addOnCompleteListener(this@MainActivity) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val intent = Intent(this@MainActivity, NavBar::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(
                                    baseContext,
                                    getString(R.string.authentication_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                                error = true
                            }
                        }
                }
            })
            { Text(getString(R.string.log_in)) }

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(onClick = {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }) {
                Icon(
                    painterResource(id = R.drawable.google_short_logo),
                    contentDescription = "Google log/sign in"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, Register::class.java)
                startActivity(intent)
            })
            { Text(getString(R.string.register)) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase

                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mauth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@MainActivity, NavBar::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                }
            }
    }
}