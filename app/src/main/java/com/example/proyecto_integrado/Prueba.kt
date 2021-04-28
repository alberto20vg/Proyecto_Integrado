package com.example.proyecto_integrado

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


class Prueba : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            RecipeColumnListDemo(recipeList)
        }
    }
}


data class Recipe(
    val prueba1: String,
    val prueba2: String
)

// [EN] Creating the dummy list
// [ES] Creamos una lista de prueba
val recipeList = listOf(
    Recipe("Arrozmate", "Arrozmate"),
    Recipe("Calabaza", "Arrozmate"),
    Recipe("Torta", "Arrozmate"),
    Recipe("Torta2", "Arrozmate"),
    Recipe("TestText", "Arrozmate"),
    Recipe("Android", "Arrozmate"),
    Recipe("Android", "Arrozmate"),
    Recipe("Android", "Arrozmate"),
    Recipe("Android", "Arrozmate"),
    Recipe("Android", "Arrozmate"),
    Recipe("Android", "Arrozmate"),
    Recipe("Android", "Arrozmate"),
    Recipe("Android", "Arrozmate")
)


// [EN] We define each row of the recyclerview
// [ES] Definimos cada fila del recyclerview
@Composable
private fun RecipeCard(recipe: Recipe) {
    val context = LocalContext.current
    Card(shape = RoundedCornerShape(8.dp), elevation = 8.dp, modifier = Modifier.padding(8.dp).clickable(onClick = {
        Toast.makeText(context, "Notificación corta", Toast.LENGTH_SHORT).show()
    })) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = recipe.prueba1, style = typography.h6)
            Spacer(modifier = Modifier.padding(16.dp))
            Text(text = recipe.prueba2, style = typography.body2)

        }
    }
}

// [EN] We create a RecipeList with LazyColumnFor (same behaviour as RecyclerView)
// [ES] Creamos una lista de recetas con LazyColumnFor (mismo comportamiento que RecyclerView)
@Composable
fun RecipeColumnListDemo(recipeList: List<Recipe>) {
    LazyColumn {
        items(recipeList) { recipe ->
            RecipeCard(recipe)
        }
    }
}