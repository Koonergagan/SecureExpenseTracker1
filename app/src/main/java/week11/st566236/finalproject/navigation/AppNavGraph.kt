package week11.st566236.finalproject.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import week11.st566236.finalproject.data.repository.AuthRepository
import week11.st566236.finalproject.ui.analytics.AnalyticsScreen
import week11.st566236.finalproject.ui.auth.BiometricScreen

import week11.st566236.finalproject.ui.auth.ForgotPasswordScreen
import week11.st566236.finalproject.ui.auth.LoginScreen
import week11.st566236.finalproject.ui.auth.RegisterScreen
import week11.st566236.finalproject.ui.expenses.ExpensesScreen
import week11.st566236.finalproject.ui.home.HomeScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()
    val repo = AuthRepository()


    val startDestination = if (repo.isUserLoggedIn()) "biometric" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("forgot") { ForgotPasswordScreen(navController) }

        composable("main") { MainScreenWithBottomNav(appNavController = navController) } // all tabs live here
        composable("biometric") {
            BiometricScreen(navController)
        }

    }
}