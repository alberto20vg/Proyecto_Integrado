package com.example.proyecto_integrado

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {

    object Account : Screen("inicio", "", Icons.Default.Home)
    object DateRange : Screen("posts", "", Icons.Default.DateRange)
    object Edit : Screen("ajustes", "", Icons.Default.Settings)
}
