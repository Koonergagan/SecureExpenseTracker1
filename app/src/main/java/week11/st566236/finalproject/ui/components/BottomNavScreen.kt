package week11.st566236.finalproject.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavScreen(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavScreen("home", "Home", Icons.Default.Home)
    object Expenses : BottomNavScreen("expenses", "Expenses", Icons.AutoMirrored.Filled.List)
    object Analytics : BottomNavScreen("analytics", "Analytics", Icons.Default.Info)
}

val bottomNavItems = listOf(
    BottomNavScreen.Home,
    BottomNavScreen.Expenses,
    BottomNavScreen.Analytics
)