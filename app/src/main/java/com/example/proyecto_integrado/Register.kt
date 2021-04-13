package com.example.proyecto_integrado

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.proyecto_integrado.ui.theme.Proyecto_IntegradoTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Register : ComponentActivity() {
    val RC_SIGN_IN = 1664
    private var mauth = Firebase.auth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)


            Proyecto_IntegradoTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    register()
                }
            }
        }
    }

    @Composable
    fun register() {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val imagen by remember { mutableStateOf((R.drawable.ic_launcher_foreground)) }
            Image(painterResource(imagen), contentDescription = null)

            Button(onClick = {
                //TODO quitar logout y hacer intent registro
                Toast.makeText(context, "cambiar boton subir imagen", Toast.LENGTH_SHORT).show()
            }) { Text(getString(R.string.upload_potho)) }
            Spacer(modifier = Modifier.height(16.dp))


            var error by remember { mutableStateOf(false) }
            val name = remember { mutableStateOf(TextFieldValue("")) }
            val user = remember { mutableStateOf(TextFieldValue("")) }
            val email = remember { mutableStateOf(TextFieldValue("")) }
            val password = remember { mutableStateOf(TextFieldValue("")) }
            val repeat_password = remember { mutableStateOf(TextFieldValue("")) }


            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = name.value,
                isError = error,
                onValueChange = {
                    name.value = it
                    error = false
                },
                label = { Text(getString(R.string.name)) })
            Spacer(modifier = Modifier.height(16.dp))
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
                //TODO quitar logout y hacer intent registro
                Toast.makeText(context, "cambiar boton registrase", Toast.LENGTH_SHORT).show()

                mauth.createUserWithEmailAndPassword(email.value.text, password.value.text)
                    .addOnCompleteListener(this@Register) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information

                            val user = mauth.currentUser

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                        }
                    }
            }) { Text(getString(R.string.register)) }
        }
    }
}