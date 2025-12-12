package week11.st566236.finalproject.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import week11.st566236.finalproject.data.repository.AuthRepository
import week11.st566236.finalproject.ui.analytics.AnalyticsScreen
import week11.st566236.finalproject.ui.auth.*
import week11.st566236.finalproject.ui.expenses.ExpensesScreen
import week11.st566236.finalproject.ui.home.HomeScreen

@Composable
fun AppNavGraph() {

    // Main NavController for the entire app navigation
    val navController = rememberNavController()
    // Repository to check authentication status
    val repo = AuthRepository()
    val context = LocalContext.current

    // Check if the user is already logged in
    val isLoggedIn = repo.isUserLoggedIn()
    println("DEBUG: isLoggedIn = $isLoggedIn")

    // Determine start destination based on login and biometric status
    val startDestination = when {
        !isLoggedIn -> {
            println("DEBUG: User not logged in, going to login")
            "login"
        }
        else -> {
            // User is logged in - check biometric preferences
            val isBiometricEnabled = BiometricManager.isBiometricEnabled(context)
            val hasSkipped = BiometricManager.hasSkippedBiometric(context)

            println("DEBUG: isBiometricEnabled = $isBiometricEnabled")
            println("DEBUG: hasSkipped = $hasSkipped")

            when {
                isBiometricEnabled -> {
                    // Biometric enabled, prompt user for authentication
                    println("DEBUG: Biometric enabled, going to biometric_auth")
                    "biometric_auth"
                }
                !hasSkipped -> {
                    // Biometric not enabled but user hasn’t skipped - show setup screen
                    println("DEBUG: Not skipped, going to biometric_setup")
                    "biometric_setup"
                }
                else -> {
                    // Biometric skipped, go straight to main screen
                    println("DEBUG: Skipped, going to main")
                    "main"
                }
            }
        }
    }

    println("DEBUG: Final startDestination = $startDestination")

    // NavHost defines navigation graph and links routes to composable screens
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Authentication screens
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("forgot") { ForgotPasswordScreen(navController) }
        // Main application screen with bottom navigation
        composable("main") { MainScreenWithBottomNav(appNavController = navController) }

        // Biometric setup and authentication screens
        composable("biometric_setup") { BiometricSetupScreen(navController) }
        composable("biometric_auth") { BiometricAuthScreen(navController) }
    }
}