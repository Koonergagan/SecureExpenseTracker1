package week11.st566236.finalproject.ui.auth

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor
import androidx.navigation.NavController

@Composable
fun BiometricScreen(
    navController: NavController,
    viewModel: BiometricViewModel = viewModel()
) {
    val context = LocalContext.current
    val canUseBiometrics by viewModel.canUseBiometrics.observeAsState(false)

    // Check if biometrics are available
    LaunchedEffect(Unit) {
        viewModel.checkBiometrics()
    }

    // Screen UI
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
                    text = "Enable Biometric Authentication?",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Use your fingerprint or face to log in quickly!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(24.dp))

                if (canUseBiometrics) {
                    Button(
                        onClick = {
                            launchBiometricPrompt(context, onSuccess = {
                                saveBiometricPreference(context, true)
                                navController.navigate("main") {
                                    popUpTo("biometric") { inclusive = true }
                                }
                            })
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enable")
                    }
                } else {
                    Text(
                        "Biometric authentication is not available on this device.",
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = {
                        saveBiometricPreference(context, false)
                        navController.navigate("main") {
                            popUpTo("biometric") { inclusive = true }
                        }
                    }
                ) {
                    Text("Skip")
                }
            }
        }
    }
}

fun launchBiometricPrompt(
    context: Context,
    onSuccess: () -> Unit
) {
    val activity = context as? FragmentActivity
        ?: throw IllegalStateException("BiometricPrompt requires a FragmentActivity context")
    val executor: Executor = ContextCompat.getMainExecutor(context)

    val biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Login")
        .setSubtitle("Use your fingerprint or face to log in")
        .setNegativeButtonText("Cancel")
        .build()

    biometricPrompt.authenticate(promptInfo)
}

fun saveBiometricPreference(context: Context, enabled: Boolean) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("biometric_enabled", enabled).apply()
}
