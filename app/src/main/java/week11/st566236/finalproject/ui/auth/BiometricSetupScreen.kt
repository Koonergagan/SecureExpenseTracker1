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
    val activity = context.findActivity() as? androidx.fragment.app.FragmentActivity

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
                    text = "Enable Biometric Login?",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Use your fingerprint for faster login next time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Check if device supports biometric
                if (BiometricManager.canUseBiometric(context)) {
                    Button(
                        onClick = {
                            if (activity != null) {
                                BiometricManager.showBiometricPrompt(
                                    activity = activity,
                                    title = "Enable Biometric Login",
                                    subtitle = "Authenticate to enable fingerprint login",
                                    onSuccess = {
                                        // Enable biometric for future logins
                                        BiometricManager.setBiometricEnabled(context, true)
                                        BiometricManager.setSkippedBiometric(context, false)
                                        navController.navigate("main") {
                                            popUpTo("biometric_setup") { inclusive = true }
                                        }
                                    },
                                    onError = { errorCode, errString ->
                                        // If user cancels or fails, mark as skipped
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
                    Text(
                        text = "Biometric authentication is not available on this device.",
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
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
                Text(
                    text = "You can enable this later in settings",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}