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

/**
 * Biometric authentication screen.
 *
 * This screen automatically triggers a biometric prompt as soon as it appears.
 * While the prompt is displayed, the user sees a loading UI with a fingerprint icon.
 *
 */
@Composable
fun BiometricAuthScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context.findActivity() as? androidx.fragment.app.FragmentActivity

    // Automatically show biometric prompt when the screen enters composition
    LaunchedEffect(Unit) {
        // Only attempt authentication if activity + biometrics are available
        if (activity != null && BiometricManager.canUseBiometric(context)) {
            BiometricManager.showBiometricPrompt(
                activity = activity,
                title = "Login with Fingerprint",
                subtitle = "Authenticate to access your account",
                onSuccess = {
                    // Successfully authenticated → navigate into the app
                    navController.navigate("main") {
                        popUpTo("biometric_auth") { inclusive = true }
                    }
                },
                onError = { errorCode, errString ->
                    // Authentication error
                    // For now, still allow the user to continue into the app
                    navController.navigate("main") {
                        popUpTo("biometric_auth") { inclusive = true }
                    }
                }
            )
        } else {
            // Device has no biometrics → skip biometric screen
            navController.navigate("main") {
                popUpTo("biometric_auth") { inclusive = true }
            }
        }
    }

    // UI displayed while waiting for biometric prompt to appear
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
                // Large fingerprint icon for visual clarity
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = "Fingerprint",
                    tint = Color(0xFF3F51B5),
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Title text
                Text(
                    text = "Waiting for Fingerprint...",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Subtext hint
                Text(
                    text = "Touch the fingerprint sensor",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Loading indicator while biometric prompt is active
                CircularProgressIndicator()
            }
        }
    }
}