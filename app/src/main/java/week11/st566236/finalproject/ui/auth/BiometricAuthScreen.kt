package week11.st566236.finalproject.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import week11.st566236.finalproject.ui.auth.BiometricManager.findActivity

@Composable
fun BiometricAuthScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context.findActivity() as? androidx.fragment.app.FragmentActivity

    // Automatically show biometric prompt when screen appears
    LaunchedEffect(Unit) {
        if (activity != null && BiometricManager.canUseBiometric(context)) {
            BiometricManager.showBiometricPrompt(
                activity = activity,
                title = "Login with Fingerprint",
                subtitle = "Authenticate to access your account",
                onSuccess = {
                    // Success - go to main screen
                    navController.navigate("main") {
                        popUpTo("biometric_auth") { inclusive = true }
                    }
                },
                onError = { errorCode, errString ->
                    // If authentication fails, allow manual login
                    // You could navigate back to login screen here
                    // For now, just go to main (or could go to login)
                    navController.navigate("main") {
                        popUpTo("biometric_auth") { inclusive = true }
                    }
                }
            )
        } else {
            // If biometric not available, go to main
            navController.navigate("main") {
                popUpTo("biometric_auth") { inclusive = true }
            }
        }
    }

    // Loading screen while showing biometric prompt
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F8FF)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = "Fingerprint",
                    tint = Color(0xFF3F51B5),
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Waiting for Fingerprint...",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Touch the fingerprint sensor",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(24.dp))
                CircularProgressIndicator()
            }
        }
    }
}