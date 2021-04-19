package com.example.proyecto_integrado

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Account: Screen("account", "Account", Icons.Default.AccountBox)
    object ThumbUp: Screen("like", "Like", Icons.Default.ThumbUp)
    object DateRange: Screen("date", "Date", Icons.Default.DateRange)
    object Edit: Screen("edit", "Edit", Icons.Default.Edit)
}