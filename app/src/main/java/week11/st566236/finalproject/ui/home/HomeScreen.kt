package week11.st566236.finalproject.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import week11.st566236.finalproject.data.util.Resource
import week11.st566236.finalproject.viewModel.ExpensesViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: ExpensesViewModel = viewModel()) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = {
                // Sign out from Firebase
                FirebaseAuth.getInstance().signOut()

                // Reset biometric preference
                val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                prefs.edit().putBoolean("biometric_enabled", false).apply()

                // Navigate to login using app-level NavController
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true } // clears entire back stack
                    launchSingleTop = true
                }
            }) {
                Text("Logout")
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome to your Secure Expense Tracker!",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (val resource = uiState.listResource) {
            is Resource.Loading -> {
                CircularProgressIndicator()
            }
            is Resource.Error -> {
                Text("Error: ${resource.message}")
            }
            is Resource.Success -> {
                val expenses = resource.data

                // Total Expenses Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val total = expenses.sumOf { it.amount }
                        Text("Total Spent", style = MaterialTheme.typography.titleMedium)
                        Text("$${"%.2f".format(total)}", style = MaterialTheme.typography.headlineSmall)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // List of categories with totals
                Text("Spending by Category", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                val grouped = expenses.groupBy { it.category }
                LazyColumn {
                    items(grouped.entries.toList()) { entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(entry.key.name)
                            Text("$${"%.2f".format(entry.value.sumOf { it.amount })}")
                        }
                        Divider()
                    }
                }
            }
        }
    }
}
