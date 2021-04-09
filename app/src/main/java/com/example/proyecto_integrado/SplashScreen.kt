package com.example.proyecto_integrado

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

import com.google.firebase.auth.FirebaseUser
import kotlin.coroutines.EmptyCoroutineContext


class SplashScreen : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    val context = this

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var handler = Handler()
            imagen()
            handler.postDelayed(
                {
                    val intent = Intent(this, MainActivity::class.java)

                    startActivity(intent)
                    finish()


                }, 1000
            )
        }
    }

    @ExperimentalAnimationApi
    @Composable
    fun imagen() {
        Card {
            var expanded by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Handler().postDelayed(Runnable {
                    expanded = true
                }, 500)
                Image(painterResource(R.drawable.ic_launcher_foreground), contentDescription = null)
                AnimatedVisibility(expanded) {
                    Text(
                        text = getString(R.string.app_name),
                        style = MaterialTheme.typography.h5
                    )
                }
            }
        }
    }
}