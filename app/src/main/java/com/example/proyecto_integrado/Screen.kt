package com.example.proyecto_integrado

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
//TODO no puedo usar datos del resource string aqui
    object Account : Screen("inicio", "", Icons.Default.Home)
    object DateRange : Screen("com/example/proyecto_integrado/posts", "", Icons.Default.DateRange)
    object Edit : Screen("ajustes", "", Icons.Default.Settings)
    object MisPosts : Screen("misPosts", "", Icons.Default.Edit)
    object favourites : Screen("favourites", "", Icons.Default.Star)

}
