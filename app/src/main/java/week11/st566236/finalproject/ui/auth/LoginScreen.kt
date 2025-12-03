package week11.st566236.finalproject.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState = viewModel.authState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(email, { email = it }, "Email")
        AppTextField(password, { password = it }, "Password")

        if (authState is Resource.Error) {
            Text(authState.message, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        AppButton(
            text = if (authState is Resource.Loading) "Loading..." else "Login"
        ) {
            if (email.isNotBlank() && password.isNotBlank()) {
                viewModel.login(email.trim(), password.trim())
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text("Create an account")
        }

        TextButton(onClick = { navController.navigate("forgot") }) {
            Text("Forgot password?")
        }
    }

    // Navigate to biometric screen on successful login
    LaunchedEffect(authState) {
        if (authState is Resource.Success) {
            navController.navigate("biometric") {
                popUpTo("login") { inclusive = true }
            }
            viewModel.clearState()
        }
    }
}
