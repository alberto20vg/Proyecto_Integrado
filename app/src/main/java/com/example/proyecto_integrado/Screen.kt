package com.example.proyecto_integrado

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "", Icons.Default.Home)
    object Posts : Screen("posts", "", Icons.Default.DateRange)
    object Settings : Screen("settings", "", Icons.Default.Settings)
    object MyPosts : Screen("myPosts", "", Icons.Default.Edit)
    object Favourites : Screen("favourites", "", Icons.Default.Star)

}
