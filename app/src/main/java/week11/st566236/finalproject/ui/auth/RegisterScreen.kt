package week11.st566236.finalproject.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState = viewModel.authState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        // Title
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        )
        Text(
            text = "Sign up to get started",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // Card around inputs
        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                // Email input
                AppTextField(email, { email = it }, "Email")
                Spacer(modifier = Modifier.height(16.dp))

                // Password input
                AppTextField(password, { password = it }, "Password")

                // Error message
                if (authState is Resource.Error) {
                    Text(
                        text = authState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Register button
                AppButton(text = if (authState is Resource.Loading) "Loading..." else "Register") {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        viewModel.register(email.trim(), password.trim())
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back to login
        TextButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally)
        ) {
            Text("Back to Login")
        }
    }

    // Navigate on success
    if (authState is Resource.Success) {
        navController.navigate("login") {
            popUpTo("register") { inclusive = true }
        }
        viewModel.clearState()
    }
}

