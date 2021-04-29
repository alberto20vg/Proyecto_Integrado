package com.example.proyecto_integrado

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.proyecto_integrado.ui.theme.Teal200
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev.chrisbanes.accompanist.coil.CoilImage

private val storageRef = Firebase.storage.reference
private var urlPhoto = ""

class Prueba : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }
}

data class Carta(
    val idAutor: String,
    val nombreJuego: String,
    val titulo: String,
    val urlJuego: String
)

@Composable
private fun RecipeCard(carta: Carta) {
    val context = LocalContext.current

    //TODO val intent = Intent(context, MainActivity::class.java)

    storageRef.child(carta.urlJuego).downloadUrl.addOnSuccessListener {
        urlPhoto = it.toString()
    }.addOnFailureListener {
    }
    Card(
        shape = RoundedCornerShape(8.dp), elevation = 8.dp, modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = {

                //TODO   intent.putExtra("usuario", carta.idAutor);

                Toast
                    .makeText(context, "Notificación corta", Toast.LENGTH_SHORT)
                    .show()
            })
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            CoilImage(
                data = urlPhoto,
                contentDescription = "juego",
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .height(150.dp)
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
            Column(
                modifier = Modifier
                    .width(150.dp)
                    .height(100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(text = carta.titulo, style = typography.h6)

                Spacer(modifier = Modifier.padding(5.dp))

                Text(text = carta.nombreJuego, style = typography.h6)

            }
            Column(
                modifier = Modifier
                    .height(150.dp)
                    .width(70.dp)
            ) {

                CoilImage(
                    data = "url de autor",
                    contentDescription = "android",
                    alignment = Alignment.TopCenter,
                    modifier = Modifier
                        .padding(16.dp)
                        .height(50.dp)
                        .width(50.dp)
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
                Text(text = "nombre del usuario", style = typography.subtitle1)
            }
        }
    }

//TODO    startActivity(intent)
}

@Composable
fun RecyclerView(lista: List<Carta>) {
    LazyColumn {
        items(lista) { recipe ->
            RecipeCard(recipe)
        }
    }
}
