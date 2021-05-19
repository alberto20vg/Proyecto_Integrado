package com.example.proyecto_integrado

import android.content.Intent
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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.proyecto_integrado.PostsQuerys.*
import com.example.proyecto_integrado.ui.theme.Proyecto_IntegradoTheme
import com.example.proyecto_integrado.ui.theme.Teal200
import com.example.proyecto_integrado.viewModels.VMCreatePost
import com.example.proyecto_integrado.viewModels.VMSettings
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dev.chrisbanes.accompanist.coil.CoilImage


private var mauth = Firebase.auth
private lateinit var googleSignInClient: GoogleSignInClient
private val db = Firebase.firestore
private var user = mauth.currentUser
private var userName = ""
private var urlPhoto2 = ""
private val storageRef = Firebase.storage.reference

class NavBar : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            //usado por settings
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

            val docRef = db.collection("users").document(user.uid)
            docRef
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        userName = document.getString("user").toString()
                    }
                }.addOnFailureListener {}

            storageRef.child(user.uid).downloadUrl.addOnSuccessListener {
                urlPhoto2 = it.toString()
            }.addOnFailureListener {
                urlPhoto2 =
                    "https://firebasestorage.googleapis.com/v0/b/proyecto-integrado-8b304.appspot.com/o/delfaut_profile.jpg?alt=media&token=44fa05a5-075b-4eea-91e7-758f2d9ea3ed"
            }
            //---usado por settings---


            Proyecto_IntegradoTheme {
                val navController = rememberNavController()

                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        topBar = {
                        },
                        bottomBar = {
                            val items = listOf(
                                Screen.Home,
                                Screen.Posts,
                                Screen.Settings
                            )
                            BottomNavigation {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentRoute =
                                    navBackStackEntry?.arguments?.getString(KEY_ROUTE)

                                items.forEach {
                                    BottomNavigationItem(
                                        icon = { Icon(it.icon, contentDescription = "") },
                                        selected = currentRoute == it.route,
                                        label = { Text(text = it.label) },
                                        onClick = {
                                            navController.popBackStack(
                                                //   navController.graph.startDestinationId, false
                                                navController.graph.startDestination, false
                                            )
                                            if (currentRoute != it.route) {
                                                navController.navigate(it.route)
                                            }
                                        })
                                }
                            }
                        }
                    ) {
                        ScreenController(navController)
                    }
                }
            }
        }
    }

    @Composable
    fun ScreenController(navController: NavHostController) {

        NavHost(navController = navController, startDestination = "home") {

            composable("home") {
                HomeScreen()
            }

            composable("posts") {
                PostsScreen()
            }

            composable("settings") {
                SettingsScreen()
            }
        }
    }

    @Composable
    fun HomeScreen(
        postsViewModel: PostsViewModel = viewModel(factory = PostsViewModelFactory(PostsRepo()))
    ) {
        when (val postsList = postsViewModel.getPostInfo().collectAsState(initial = null).value) {

            is OnError -> {
                Text(text = "Please try after sometime")
            }

            is OnSuccess -> {
                val listOfPosts = postsList.querySnapshot?.toObjects(Posts::class.java)
                listOfPosts?.let {
                    Column {
                        LazyColumn(modifier = Modifier.fillMaxHeight()) {
                            items(listOfPosts) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(6.dp),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    RecyclerCard(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    class PostsViewModelFactory(private val postsRepo: PostsRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
                return PostsViewModel(postsRepo) as T
            }
            throw IllegalStateException()
        }
    }

    @Composable
    fun PostsScreen() {
        val navController = rememberNavController()
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                Modifier
                    .focusModifier()
                    .fillMaxHeight(0.94f),
                bottomBar = {
                },
                topBar = {
                    val items = listOf(
                        Screen.MyPosts,
                        Screen.Favourites,
                    )
                    BottomNavigation {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute =
                            navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                        items.forEach {
                            BottomNavigationItem(
                                icon = { Icon(it.icon, contentDescription = "") },
                                selected = currentRoute == it.route,
                                label = { Text(text = it.label) },
                                onClick = {
                                    navController.popBackStack(
                                        navController.graph.startDestination, false
                                    )
                                    if (currentRoute != it.route) {
                                        navController.navigate(it.route)
                                    }
                                })
                        }
                    }
                }
            ) {
                ScreenController2(navController)
            }
        }
    }

    @Composable
    fun ScreenController2(navController: NavHostController) {

        NavHost(navController = navController, startDestination = "myPosts") {

            composable("myPosts") {
                MisPostScreen()
            }

            composable("favourites") {
                FavouritesScreen()
            }
        }
    }

    @Composable
    fun FavouritesScreen(
        postsViewModel: PostsViewModel = viewModel(factory = PostsViewModelFactory(PostsRepo()))
    ) {
        when (val postsList = postsViewModel.getStarPosts().collectAsState(initial = null).value) {

            is OnError -> {
                Text(text = "Please try after sometime")
            }

            is OnSuccess -> {
                val listOfPosts = postsList.querySnapshot?.toObjects(Posts::class.java)
                listOfPosts?.let {
                    Column {
                        LazyColumn(modifier = Modifier.fillMaxHeight()) {
                            items(listOfPosts) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(6.dp),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    RecyclerCard(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MisPostScreen(
        postsViewModel: PostsViewModel = viewModel(
            factory = PostsViewModelFactory(
                PostsRepo()
            )
        )
    ) {
        val context = LocalContext.current
        Scaffold(
            backgroundColor = Color(0xFFFEFEFA),
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val intent = Intent(context, CreatePost::class.java)
                        startActivity(intent)
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Localized description")
                }
            }
        ) {
            when (val postsList =
                postsViewModel.getMyPostInfo().collectAsState(initial = null).value) {

                is OnError -> {
                    Text(text = "Please try after sometime")
                }

                is OnSuccess -> {
                    val listOfPosts = postsList.querySnapshot?.toObjects(Posts::class.java)
                    listOfPosts?.let {
                        Column {
                            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                                items(listOfPosts) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(6.dp),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        RecyclerCard(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    @Composable
    private fun RecyclerCard(post: Posts) {
        val context = LocalContext.current
        Card(
            shape = RoundedCornerShape(8.dp), elevation = 8.dp, modifier = Modifier
                .padding(8.dp)
                .clickable(onClick = {

                    val intent = Intent(context, VistaPost::class.java)
                    intent.putExtra("idPost", post.autor);
                    startActivity(intent)
                })
        ) {

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {

                CoilImage(
                    data = post.urlPhotoJuego,
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
                    },
                    error = {
                        Box(
                            modifier = Modifier.background(
                                color = Teal200
                            )
                        )
                    }
                )

                Column(
                    modifier = Modifier
                        .width(150.dp)
                        .height(100.dp)
                        .align(Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Text(text = post.title, style = MaterialTheme.typography.h6)

                    Spacer(modifier = Modifier.padding(5.dp))

                    Text(text = post.gameName, style = MaterialTheme.typography.h6)
                }

                Column(
                    modifier = Modifier
                        .height(150.dp)
                        .width(70.dp)
                ) {

                    CoilImage(
                        data = post.urlPhotoUser,
                        contentDescription = "android",
                        alignment = Alignment.BottomCenter,
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
                    Text(
                        text = post.userName,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                }
            }
        }

    }

    @Composable
    fun SettingsScreen(model: VMSettings = viewModel()) {
        val context = LocalContext.current
        val checkedState1 = remember { mutableStateOf(false) }
        val checkedState2 = remember { mutableStateOf(false) }

        val userNameVM by model.userNameLiveData.observeAsState(userName)
        val urlPhotoVM by model.urlPhoto2LiveData.observeAsState(urlPhoto2)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {

            Text(
                text = getString(R.string.account),
                style = TextStyle(color = Color.Black, fontSize = 36.sp),
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(16.dp)
            )

            Divider(color = Color.Black, thickness = 1.dp)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {

                CoilImage(
                    data = if (user.providerData[1].providerId == "google.com") {
                        user.photoUrl
                    } else {
                        urlPhotoVM
                    },
                    contentDescription = "android",
                    alignment = Alignment.TopCenter,
                    modifier = Modifier
                        .padding(16.dp)
                        .height(100.dp)
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

                Column {

                    Text(
                        text = user.email,
                        style = TextStyle(color = Color.Black),
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(16.dp)
                    )

                    Text(
                        text = if (user.providerData[1].providerId == "google.com") {
                            user.displayName
                        } else {
                            userNameVM
                        },
                        style = TextStyle(color = Color.Black),
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
                onClick = {
                    mauth.signOut()
                    googleSignInClient.revokeAccess()
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    model.setUserName("")
                    model.setUrlPhoto2("")
                    //TODO esto sigue sin ir viewModel implementado
                }) { Text(getString(R.string.logout)) }

            Divider(color = Color.White)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = getString(R.string.notifications),
                style = TextStyle(color = Color.Black, fontSize = 36.sp),
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(16.dp)
            )

            Divider(color = Color.Black, thickness = 1.dp)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = getString(R.string.push_notifications_1),
                    style = TextStyle(color = Color.Black),
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(16.dp)
                )

                Switch(
                    checked = checkedState1.value,
                    onCheckedChange = {
                        checkedState1.value = it
                        if (checkedState1.value) Toast.makeText(
                            baseContext, "Checker",
                            Toast.LENGTH_SHORT
                        ).show() else Toast.makeText(
                            baseContext, "Unchecked",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = getString(R.string.push_notifications_2),
                    style = TextStyle(color = Color.Black),
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(16.dp)
                )

                Switch(
                    checked = checkedState2.value,
                    onCheckedChange = {
                        checkedState2.value = it
                        if (checkedState2.value) Toast.makeText(
                            baseContext, "Checker",
                            Toast.LENGTH_SHORT
                        ).show() else Toast.makeText(
                            baseContext, "Unchecked",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}