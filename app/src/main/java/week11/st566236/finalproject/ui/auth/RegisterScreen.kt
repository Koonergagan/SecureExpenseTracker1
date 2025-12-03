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
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel = viewModel()) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState = viewModel.authState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)

        AppTextField(email, { email = it }, "Email")
        AppTextField(password, { password = it }, "Password")

        if (authState is Resource.Error) {
            Text(authState.message, color = Color.Red)
        }

        AppButton(text = if (authState is Resource.Loading) "Loading..." else "Register") {
            if (email.isNotBlank() && password.isNotBlank()) {
                viewModel.register(email.trim(), password.trim())
            }
        }

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Back to Login")
        }
    }

    // When successful → redirect to login
    if (authState is Resource.Success) {
        navController.navigate("login") {
            popUpTo("register") { inclusive = true }
        }
        viewModel.clearState()
    }
}