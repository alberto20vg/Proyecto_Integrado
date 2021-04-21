package com.example.proyecto_integrado

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    //TODO no puedo usar bien los strings
    object Account: Screen("inicio", R.string.start.toString(), Icons.Default.Home)
    object DateRange: Screen("posts", R.string.posts.toString(), Icons.Default.DateRange)
    object Edit: Screen("ajustes", R.string.settings.toString(), Icons.Default.Settings)
}
