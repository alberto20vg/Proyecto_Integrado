package com.example.proyecto_integrado

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_integrado.CommentsPackage.CommentsRepo
import com.example.proyecto_integrado.CommentsPackage.CommentsViewModel
import com.example.proyecto_integrado.ui.theme.Teal200
import com.example.proyecto_integrado.viewModels.VMViewPost
import com.google.firebase.auth.ktx.auth
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlin.collections.ArrayList
import com.example.proyecto_integrado.CommentsPackage.*

private val db = Firebase.firestore
private var mauth = Firebase.auth
private var user = mauth.currentUser

class VistaPost : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val objetIntent: Intent = intent
        val valor = objetIntent.getStringExtra("idPost").toString()

        setContent {
            vistaPost(valor)
        }
    }

    @Composable
    fun vistaPost(valor: String, model: VMViewPost = viewModel()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val docRef = db.collection("posts").document(valor)
            docRef
                .get()
                .addOnSuccessListener { document ->
                    model.setUrlPhotoGame(document.getString("urlPhotoJuego").toString())
                    model.setTitle(document.getString("title").toString())
                    model.setTextReview(document.getString("textReview").toString())
                }.addOnFailureListener {}

            val docRef2 = db.collection("users").document(user.uid)
            docRef2
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        model.setStarPosts(document.get("starPosts") as ArrayList<String>)
                    }
                }.addOnFailureListener {}

            val urlPhotoGame by model.urlPhotoGameLiveData.observeAsState("")
            val textReview by model.textReviewLiveData.observeAsState("")
            val title by model.titleLiveData.observeAsState("")
            val starPhotoPost by model.starPhotoPostLiveData.observeAsState("https://firebasestorage.googleapis.com/v0/b/proyecto-integrado-8b304.appspot.com/o/empty_star.png?alt=media&token=f73c0975-9d40-44ba-9221-5c7f92cf8764")
            val starPosts by model.starPostsLiveData.observeAsState(null)

            if (starPosts?.contains(valor) == true) {
                model.setStarPhotoPost("https://firebasestorage.googleapis.com/v0/b/proyecto-integrado-8b304.appspot.com/o/full_star.png?alt=media&token=164420ba-1863-4951-8741-f6582bf8c789")
            }

            CoilImage(
                data = urlPhotoGame,
                contentDescription = "game",
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .height(150.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.background(
                            color = Teal200
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.padding(16.dp))

            CoilImage(
                data = starPhotoPost,
                contentDescription = "game",
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        model.setStarPhotoPost()
                        if (starPhotoPost == "https://firebasestorage.googleapis.com/v0/b/proyecto-integrado-8b304.appspot.com/o/full_star.png?alt=media&token=164420ba-1863-4951-8741-f6582bf8c789") {
                            starPosts?.add(valor)
                            db
                                .collection("users")
                                .document(user.uid)
                                .update("starPosts", starPosts)
                        } else {
                            starPosts?.remove(valor)
                            db
                                .collection("users")
                                .document(user.uid)
                                .update("starPosts", starPosts)
                        }
                    },
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.background(
                            color = Teal200
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Text(
                text = textReview,
                style = MaterialTheme.typography.h5
            )
            CommentsList(valor)

            //TODO cosas para postear
        }
    }

    @Composable
    fun CommentsList(valor:String,commentsViewModel: CommentsViewModel = viewModel(factory = CommentsViewModelFactory(CommentsRepo()))
    ) {

        val idPost by commentsViewModel.idPostLiveData.observeAsState(valor)
        commentsViewModel.setIdPost(valor)

        when (val commentList =
            commentsViewModel.getCommentsInfo(valor).collectAsState(initial = null).value) {

            is OnError -> {
                Text(text = "Please try after sometime")
            }

            is OnSuccess -> {
                val listOfComments = commentList.querySnapshot?.toObjects(Comments::class.java)
                listOfComments?.let {
                    Column(modifier = Modifier.height(100.dp)) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(20.dp)
                        ) {
                            items(listOfComments) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                ) {
                                    CommentsDetails(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CommentsDetails(comments: Comments) {
        //TODO terminar de maquetar esto
        Column(modifier = Modifier.clickable {
        }) {
            Text(
                text = comments.text,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }

    class CommentsViewModelFactory(private val commentsRepo: CommentsRepo) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
                return CommentsViewModel(commentsRepo) as T
            }
            throw IllegalStateException()
        }
    }
}