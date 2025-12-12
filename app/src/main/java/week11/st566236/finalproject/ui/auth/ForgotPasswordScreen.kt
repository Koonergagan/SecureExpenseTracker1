package week11.st566236.finalproject.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import week11.st566236.finalproject.data.util.Resource
import week11.st566236.finalproject.ui.components.AppButton
import week11.st566236.finalproject.ui.components.AppTextField
import week11.st566236.finalproject.viewModel.AuthViewModel

@Composable
fun ForgotPasswordScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {

    // Local state for email input field
    var email by remember { mutableStateOf("") }
    // Collect the authentication state from ViewModel
    val authState = viewModel.authState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Screen title
        Text("Reset Password", style = MaterialTheme.typography.headlineMedium)

        // Email text field
        AppTextField(email, { email = it }, "Email")

        // Display error if reset failed
        if (authState is Resource.Error) {
            Text(authState.message, color = Color.Red)
        }

        // Show success confirmation
        if (authState is Resource.Success) {
            Text("Reset link sent!", color = Color.Green)
        }

        // Main button: triggers password reset request
        AppButton(text = if (authState is Resource.Loading) "Sending..." else "Send Reset Email") {
            if (email.isNotBlank()) viewModel.resetPassword(email.trim())
        }

        // Navigate back to login screen
        TextButton(onClick = { navController.popBackStack() }) {
            Text("Back to Login")
        }
    }

    // Clear state after success so the message doesn't persist on navigation
    if (authState is Resource.Success) {
        viewModel.clearState()
    }
}