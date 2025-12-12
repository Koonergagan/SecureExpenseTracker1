package week11.st566236.finalproject.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Sealed class representing the screens in the bottom navigation bar.
 *
 */
sealed class BottomNavScreen(val route: String, val label: String, val icon: ImageVector) {

    // Home Screen
    object Home : BottomNavScreen("home", "Home", Icons.Default.Home)

    // Expenses Screen
    object Expenses : BottomNavScreen("expenses", "Expenses", Icons.AutoMirrored.Filled.List)

    // Analytics Screen
    object Analytics : BottomNavScreen("analytics", "Analytics", Icons.Default.Info)
}

// List of screens to display in the bottom navigation bar
val bottomNavItems = listOf(
    BottomNavScreen.Home,
    BottomNavScreen.Expenses,
    BottomNavScreen.Analytics
)