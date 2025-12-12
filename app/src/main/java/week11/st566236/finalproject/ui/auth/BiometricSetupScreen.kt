package week11.st566236.finalproject.ui.auth



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import week11.st566236.finalproject.ui.auth.BiometricManager.findActivity

@Composable
fun BiometricSetupScreen(navController: NavController) {
    val context = LocalContext.current
    // Find hosting FragmentActivity
    val activity = context.findActivity() as? androidx.fragment.app.FragmentActivity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F8FF)),
        contentAlignment = Alignment.Center
    ) {
        // Main card container
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
                // Fingerprint icon
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = "Fingerprint",
                    tint = Color(0xFF3F51B5),
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Title
                Text(
                    text = "Enable Biometric Login?",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Explanation text
                Text(
                    text = "Use your fingerprint for faster login next time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Check if device supports biometric
                if (BiometricManager.canUseBiometric(context)) {
                    // Enable biometric button
                    Button(
                        onClick = {
                            if (activity != null) {
                                // Launch BiometricPrompt
                                BiometricManager.showBiometricPrompt(
                                    activity = activity,
                                    title = "Enable Biometric Login",
                                    subtitle = "Authenticate to enable fingerprint login",
                                    onSuccess = {
                                        // Save preference so future logins use biometrics
                                        BiometricManager.setBiometricEnabled(context, true)
                                        BiometricManager.setSkippedBiometric(context, false)
                                        // Navigate to main screen + remove this screen from back stack
                                        navController.navigate("main") {
                                            popUpTo("biometric_setup") { inclusive = true }
                                        }
                                    },
                                    onError = { errorCode, errString ->
                                        // Mark biometric as skipped if user cancels or fails
                                        BiometricManager.setSkippedBiometric(context, true)
                                        navController.navigate("main") {
                                            popUpTo("biometric_setup") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enable Fingerprint")
                    }
                } else {
                    // Device does not support biometrics
                    Text(
                        text = "Biometric authentication is not available on this device.",
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                // Skip biometric setup
                TextButton(
                    onClick = {
                        BiometricManager.setSkippedBiometric(context, true)
                        navController.navigate("main") {
                            popUpTo("biometric_setup") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Skip for now")
                }

                Spacer(modifier = Modifier.height(8.dp))
                // Informational message
                Text(
                    text = "You can enable this later in settings",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}