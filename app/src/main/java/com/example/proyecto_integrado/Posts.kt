package com.example.proyecto_integrado

import android.widget.Toast
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

@Composable
fun ToolbarDemo() {
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
            ScreenController(navController)
        }
    }
}

@Composable
fun ScreenController(navController: NavHostController) {

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
                    Toast.makeText(
                        context,
                        "Clicked",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //TODO buscar si puedo poner un modificador para que salga bien el fabButton
                //   , modifier = Modifier.padding(expandVertically(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Localized description")
            }
        }
    ) { RecyclerView(listaInicio) }
}