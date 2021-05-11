package com.example.proyecto_integrado

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.proyecto_integrado.ui.theme.Proyecto_IntegradoTheme
import com.example.proyecto_integrado.ui.theme.Teal200
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
private var nombreUsuario = ""
private var urlPhoto = ""
private var urlPhoto2 = ""
private val storageRef = Firebase.storage.reference

//TODO hacer privada cuando modifique las listas de la clase posts
var listaInicio: MutableList<Carta> = mutableListOf()

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
                        nombreUsuario = document.getString("user").toString()
                    }
                }.addOnFailureListener {}

            storageRef.child(user.uid).downloadUrl.addOnSuccessListener {
                // Got the download URL for 'users/me/profile.png'
                urlPhoto2 = it.toString()
            }.addOnFailureListener {
            }
            //---usado por settings---


            //usado por inicio
            val docRef2 = db.collection("posts")
            docRef2
                .get()
                .addOnSuccessListener { document ->
                    for (i in document) {

                        val aux =
                            Carta(
                                i.id,
                                i.getString("nombreJuego")!!,
                                i.getString("titulo")!!,
                                i.getString("urlPhotoJuego")!!,
                                i.getString("urlPhotoUser")!!,
                                i.getString("userName")!!
                            )

                        if (aux != null) {
                            listaInicio.add(aux)
                        }
                    }

                }.addOnFailureListener {
                    Toast.makeText(this, "Notificación corta", Toast.LENGTH_SHORT).show()
                }
            //---usado por inicio---


            Proyecto_IntegradoTheme {

                val navController = rememberNavController()

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        topBar = {
                        },
                        bottomBar = {
                            val items = listOf(
                                Screen.Account,
                                Screen.DateRange,
                                Screen.Edit,
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
                        ScreenController(navController)
                    }
                }
            }
        }
    }

    @Composable
    fun ScreenController(navController: NavHostController) {

        NavHost(navController = navController, startDestination = "inicio") {

            composable("inicio") {
                StartScreen()
            }

            composable("com/example/proyecto_integrado/posts") {
                PostsScreen()
            }

            composable("ajustes") {
                SettingsScreen()
            }
        }
    }

    @Composable
    fun StartScreen() {
        //TODO no se carga la primera vez que se ve y deberia poner para que se refresque en tiempo real y se pare si la pantalla se quita
        RecyclerView(listaInicio)

    }

    @Composable
    fun PostsScreen() {
        val navController = rememberNavController()
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                bottomBar = {
                },
                topBar = {
                    val items = listOf(
                        Screen.MisPosts,
                        Screen.favourites,
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

        NavHost(navController = navController, startDestination = "misPosts") {

            composable("misPosts") {
                MisPostScreen()
            }

            composable("favourites") {
                FavouritesScreen()
            }
        }
    }

    @Composable
    fun FavouritesScreen() {
        RecyclerView(listaInicio)
    }

    @Composable
    fun MisPostScreen() {
        val context = LocalContext.current
        Scaffold(
            backgroundColor = Color(0xFFFEFEFA),
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val intent = Intent(context, CrearPost::class.java)
                        startActivity(intent)
                    }
                    //TODO buscar si puedo poner un modificador para que salga bien el fabButton
                    //   , modifier = Modifier.padding(expandVertically(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Localized description")
                }
            }
        ) { RecyclerView(listaInicio) }
    }

    @Composable
    private fun RecipeCard(carta: Carta) {
        val context = LocalContext.current

        storageRef.child(carta.urlJuego).downloadUrl.addOnSuccessListener {
            urlPhoto = it.toString()
        }.addOnFailureListener {
        }

        Card(
            shape = RoundedCornerShape(8.dp), elevation = 8.dp, modifier = Modifier
                .padding(8.dp)
                .clickable(onClick = {

                    val intent = Intent(context, VistaPost::class.java)
                    intent.putExtra("idPost", carta.idPost);
                    startActivity(intent)
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

                    Text(text = carta.titulo, style = MaterialTheme.typography.h6)

                    Spacer(modifier = Modifier.padding(5.dp))

                    Text(text = carta.nombreJuego, style = MaterialTheme.typography.h6)
                }

                Column(
                    modifier = Modifier
                        .height(150.dp)
                        .width(70.dp)
                ) {

                    CoilImage(
                        data = carta.urlUser,
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
                    Text(text = carta.nameUser, style = MaterialTheme.typography.subtitle1)
                }
            }
        }

    }

    @Composable
    fun RecyclerView(lista: List<Carta>) {
        LazyColumn {
            items(lista) { recipe ->
                RecipeCard(recipe)
            }
        }
    }

    @Composable
    fun SettingsScreen() {
//TODO deberia poder llamar aqui a un metodo en otra clase
        val context = LocalContext.current
        val checkedState1 = remember { mutableStateOf(false) }
        val checkedState2 = remember { mutableStateOf(false) }

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
                        urlPhoto2
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
                            nombreUsuario
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
                    //TODO Sigue guardando las variables anteriores poner a null
                    //TODO poner string
                }) { Text("cerrar sesion") }

            //TODO no me lo pone blanco DIVIDER
            Divider(
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                }, thickness = 3.dp
            )

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
    }//TODO aqui termina el settings
}