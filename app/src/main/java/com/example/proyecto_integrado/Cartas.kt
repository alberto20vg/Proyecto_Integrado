package com.example.proyecto_integrado

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.proyecto_integrado.ui.theme.Teal200
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev.chrisbanes.accompanist.coil.CoilImage

private val storageRef = Firebase.storage.reference
private var urlPhoto = ""

data class Carta(
    val idPost: String,
    val nombreJuego: String,
    val titulo: String,
    val urlJuego: String,
    var urlUser: String,
    var nameUser: String
)


