package week11.st566236.finalproject.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import week11.st566236.finalproject.ui.analytics.AnalyticsScreen
import week11.st566236.finalproject.ui.components.BottomNavScreen
import week11.st566236.finalproject.ui.components.bottomNavItems
import week11.st566236.finalproject.ui.expenses.ExpensesScreen
import week11.st566236.finalproject.ui.home.HomeScreen
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.navigation.NavController

@Composable
fun MainScreenWithBottomNav(appNavController: NavController) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavScreen.Home.route) { HomeScreen(navController = appNavController) }
            composable(BottomNavScreen.Expenses.route) { ExpensesScreen() }
            composable(BottomNavScreen.Analytics.route) { AnalyticsScreen() }
        }
    }
}