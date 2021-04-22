package com.example.proyecto_integrado

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
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
var user = mauth.currentUser
var nombreUsuario = ""
var urlPhoto = ""
private val storageRef = Firebase.storage.reference

class NavBar : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

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
                    } else {

                    }
                }
                .addOnFailureListener { exception ->

                }
            storageRef.child(user.uid).downloadUrl.addOnSuccessListener {
                // Got the download URL for 'users/me/profile.png'
                urlPhoto = it.toString()
            }.addOnFailureListener {
                // Handle any errors
            }

            Proyecto_IntegradoTheme {
                val navController = rememberNavController()
                val title = remember { mutableStateOf("Account") }

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
                        ScreenController(navController, title)
                    }
                }
            }
        }
    }

    @Composable
    fun ScreenController(navController: NavHostController, topBarTitle: MutableState<String>) {
        NavHost(navController = navController, startDestination = "inicio") {
            composable("inicio") {
                StartScreen()
            }

            composable("posts") {
                PostsScreen()
            }

            composable("ajustes") {
                SettingsScreen()
            }
        }
    }

    @Composable
    fun StartScreen() {
        Text(
            text = "Account",
            style = TextStyle(fontSize = 36.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
        )
    }

    @Composable
    fun PostsScreen() {
        Text(
            text = "Date",
            style = TextStyle(fontSize = 36.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
        )
    }

    @Composable
    fun SettingsScreen() {
        var context = LocalContext.current
        val checkedState1 = remember { mutableStateOf(false) }
        val checkedState2 = remember { mutableStateOf(false) }



        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            Text(
                //TODO añadir string
                text = "Cuenta",
                style = TextStyle(color = Color.Black, fontSize = 36.sp),
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(16.dp)
            )
            Divider(color = Color.Black, thickness = 1.dp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
            //TODO no consigo que la foto salga segun si es google o firebase
                CoilImage(
                    data = /* urlPhoto */ user.photoUrl,
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
                        //TODO usar firebase

                        text = /* nombreUsuario */ user.displayName,
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
                //TODO añadir string
                text = "Notificaciones",
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
                    //TODO añadir string
                    text = "Comentan en un post",
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
                    //TODO añadir string
                    text = "Dan like a un post",
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



