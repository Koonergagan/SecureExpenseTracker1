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
/*
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val repo = AuthRepository()
    val context = LocalContext.current

    // Check if user is logged in and biometric status
    val isLoggedIn = repo.isUserLoggedIn()
    val isBiometricEnabled = BiometricManager.isBiometricEnabled(context)
    val hasSkipped = BiometricManager.hasSkippedBiometric(context)

    // Determine start destination
    val startDestination = when {
        !isLoggedIn -> "login"
        isBiometricEnabled -> "biometric_auth" // Show biometric prompt on login
        !hasSkipped -> "biometric_setup" // Ask to enable biometric (first time)
        else -> "main" // Already skipped biometric
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("forgot") { ForgotPasswordScreen(navController) }
        composable("main") { MainScreenWithBottomNav(appNavController = navController) }

        // NEW: Screen to ask user to enable biometric
        composable("biometric_setup") {
            BiometricSetupScreen(navController)
        }

        // NEW: Screen to authenticate with biometric
        composable("biometric_auth") {
            BiometricAuthScreen(navController)
        }
    }
}
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val repo = AuthRepository()
    val context = LocalContext.current

    // Check if user is logged in and biometric status
    val isLoggedIn = repo.isUserLoggedIn()
    val isBiometricEnabled = BiometricManager.isBiometricEnabled(context)
    val hasSkipped = BiometricManager.hasSkippedBiometric(context)

    // Determine start destination
    val startDestination = when {
        !isLoggedIn -> "login"
        isBiometricEnabled -> "biometric_auth" // Show biometric prompt on login
        !hasSkipped -> "biometric_setup" // Ask to enable biometric (first time)
        else -> "main" // Already skipped biometric, go directly to main
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("forgot") { ForgotPasswordScreen(navController) }
        composable("main") { MainScreenWithBottomNav(appNavController = navController) }

        composable("biometric_setup") {
            BiometricSetupScreen(navController)
        }

        composable("biometric_auth") {
            BiometricAuthScreen(navController)
        }
    }
}*/
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val repo = AuthRepository()
    val context = LocalContext.current

    // Check login status
    val isLoggedIn = repo.isUserLoggedIn()
    println("DEBUG: isLoggedIn = $isLoggedIn")

    // Start destination logic
    val startDestination = when {
        !isLoggedIn -> {
            println("DEBUG: User not logged in, going to login")
            "login"
        }
        else -> {
            // User IS logged in - check biometric status
            val isBiometricEnabled = BiometricManager.isBiometricEnabled(context)
            val hasSkipped = BiometricManager.hasSkippedBiometric(context)

            println("DEBUG: isBiometricEnabled = $isBiometricEnabled")
            println("DEBUG: hasSkipped = $hasSkipped")

            when {
                isBiometricEnabled -> {
                    println("DEBUG: Biometric enabled, going to biometric_auth")
                    "biometric_auth"
                }
                !hasSkipped -> {
                    println("DEBUG: Not skipped, going to biometric_setup")
                    "biometric_setup"
                }
                else -> {
                    println("DEBUG: Skipped, going to main")
                    "main"
                }
            }
        }
    }

    println("DEBUG: Final startDestination = $startDestination")

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("forgot") { ForgotPasswordScreen(navController) }
        composable("main") { MainScreenWithBottomNav(appNavController = navController) }

        // MAKE SURE THESE ARE DEFINED:
        composable("biometric_setup") { BiometricSetupScreen(navController) }
        composable("biometric_auth") { BiometricAuthScreen(navController) } // <-- THIS WAS MISSING!
        // ... rest of your composables
    }
}