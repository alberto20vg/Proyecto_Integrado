package com.example.proyecto_integrado

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_integrado.GamesPackage.*
import com.example.proyecto_integrado.viewModels.VMCreatePost
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

private var mauth = Firebase.auth
private val db = Firebase.firestore
private var user = mauth.currentUser
private val storageRef = Firebase.storage.reference
private var urlPhoto = ""
private var userName = ""

class CreatePost : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            createPost()

            storageRef.child(user.uid).downloadUrl.addOnSuccessListener {
                urlPhoto = it.toString()
            }.addOnFailureListener {
            }

            val docRef = db.collection("users").document(user.uid)
            docRef
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        userName = document.getString("user").toString()
                    }
                }.addOnFailureListener {}
        }
    }

    @Composable
    fun createPost(model: VMCreatePost = viewModel()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val gameName by model.gameNameLiveData.observeAsState("")
            val urlPhotoGame by model.urlPhotoGameLiveData.observeAsState("")

            var title by remember {
                mutableStateOf("")
            }
            var text by remember {
                mutableStateOf("")
            }

            GamesList()

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

            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(getString(R.string.enter_text)) },
                maxLines = 25,
                singleLine = false,
                textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(20.dp)
                    .height(150.dp)
            )

            if (user.providerData[1].providerId == "google.com") {
                urlPhoto = user.photoUrl.toString()
            } else {
                urlPhoto
            }

            if (user.providerData[1].providerId == "google.com") {
                userName = user.displayName
            } else {
                userName
            }

            Button(onClick = {
                storageRef.child(urlPhotoGame).downloadUrl.addOnSuccessListener {
                    val arrayList = ArrayList<String>()
                    arrayList.add("")

                    val data = hashMapOf(
                        "autor" to user.uid,
                        "textReview" to text,
                        "title" to title,
                        "urlPhotoUser" to urlPhoto,
                        "userName" to userName,
                        "email" to user.email,
                        "comentarios" to arrayList,
                        "urlPhotoJuego" to it.toString(),
                        "gameName" to gameName,
                        "postId" to ""
                    )

                    db.collection("posts").add(data).addOnSuccessListener {
                        db.collection("posts").document(it.id).update("postId", it.id)
                    }
                }.addOnFailureListener {
                }

                val intent = Intent(this@CreatePost, NavBar::class.java)
                startActivity(intent)
                finish()
            })
            {
                Text(
                    getString(R.string.post)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@CreatePost, AddGame::class.java)
                startActivity(intent)
            })
            {
                Text(
                    getString(R.string.add_game)
                )
            }
        }
    }

    @Composable
    fun GamesList(
        gamesViewModel: GamesViewModel = viewModel(factory = GamesViewModelFactory(GamesRepo()))
    ) {

        when (val gamesList =
            gamesViewModel.getGamesInfo().collectAsState(initial = null).value) {

            is OnError -> {
                Text(text = "Please try after sometime")
            }

            is OnSuccess -> {
                val listOfGames = gamesList.querySnapshot?.toObjects(Games::class.java)
                listOfGames?.let {
                    Column(modifier = Modifier.height(100.dp)) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(20.dp)
                        ) {
                            items(listOfGames) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                ) {
                                    GamesDetails(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun GamesDetails(game: Games, model: VMCreatePost = viewModel()) {
        val context = LocalContext.current
        Column(modifier = Modifier.clickable {
        }) {
            Text(
                text = game.gameName,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        Toast
                            .makeText(
                                context,
                                game.gameName, Toast.LENGTH_SHORT
                            )
                            .show();
                        model.setGameName(game.gameName)
                        model.setUrlPhotoGame(game.urlGame)
                    })
        }
    }

    class GamesViewModelFactory(private val gamesRepo: GamesRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GamesViewModel::class.java)) {
                return GamesViewModel(gamesRepo) as T
            }
            throw IllegalStateException()
        }
    }
}