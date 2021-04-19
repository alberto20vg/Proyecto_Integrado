package com.example.proyecto_integrado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.proyecto_integrado.ui.theme.Proyecto_IntegradoTheme

class NavBar : ComponentActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Proyecto_IntegradoTheme {

                    val navController = rememberNavController()
                    val title = remember { mutableStateOf("Account") }

                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {

                        Scaffold(
                            topBar = {
                                TopAppBar(title = { Text(text = title.value) },
                                    actions = {
                                        IconButton(onClick = {}) {
                                            Icon(Icons.Default.Email, contentDescription = "hola")
                                        }
                                    })
                            },

                            bottomBar = {

                                val items = listOf(
                                    Screen.Account,
                                    Screen.DateRange,
                                    Screen.Edit,
                                    Screen.ThumbUp
                                )
                                BottomNavigation {
                                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                                    val currentRoute =
                                        navBackStackEntry?.arguments?.getString(KEY_ROUTE)

                                    items.forEach {
                                        BottomNavigationItem(
                                            icon = { Icon(it.icon, contentDescription = "hola") },
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
    }

    @Composable
    fun ScreenController(navController: NavHostController, topBarTitle: MutableState<String>) {
        NavHost(navController = navController, startDestination = "account") {
            composable("account") {
                AccountScreen()
                topBarTitle.value = "Account"
            }

            composable("date") {
                DateScreen()
                topBarTitle.value = "Date"
            }

            composable("edit") {
                EditScreen()
                topBarTitle.value = "Edit"
            }

            composable("like") {
                ThumpUpScreen()
                topBarTitle.value = "Like"
            }
        }
    }

    @Composable
    fun AccountScreen() {
        Text(
            text = "Account",
            style = TextStyle(color = Color.Black, fontSize = 36.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Cyan)
        )
    }

    @Composable
    fun DateScreen() {
        Text(
            text = "Date",
            style = TextStyle(color = Color.Black, fontSize = 36.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Magenta)
        )
    }

    @Composable
    fun EditScreen() {
        Text(
            text = "Edit",
            style = TextStyle(color = Color.Black, fontSize = 36.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        )
    }

    @Composable
    fun ThumpUpScreen() {
        Text(
            text = "ThumpUp",
            style = TextStyle(color = Color.Black, fontSize = 36.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green)
        )
    }