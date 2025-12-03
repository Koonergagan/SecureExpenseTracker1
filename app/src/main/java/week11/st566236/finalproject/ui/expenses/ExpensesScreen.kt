package week11.st566236.finalproject.ui.expenses

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st566236.finalproject.data.model.Expense
import week11.st566236.finalproject.data.util.Resource
import week11.st566236.finalproject.viewModel.ExpensesViewModel

@Composable
fun ExpensesScreen(
    viewModel: ExpensesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedExpense by remember { mutableStateOf<Expense?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main list of expenses
        when (val resource = uiState.listResource) {
            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${resource.message}")
                }
            }
            is Resource.Success -> {
                val expenses = resource.data
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(expenses) { expense ->
                        ExpenseItemRow(
                            expense = expense,
                            onDelete = { viewModel.deleteExpense(expense.id) },
                            onEdit = {
                                selectedExpense = expense
                                showEditDialog = true
                            }
                        )
                        Divider()
                    }
                }
            }
        }

        // Floating "Add Expense" button
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Expense")
        }
    }

    // Add Expense Dialog
    if (showAddDialog) {
        AddExpenseDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, amount, category, date ->
                viewModel.addExpense(title, amount, category, date)
                showAddDialog = false
            }
        )
    }

    // Edit Expense Dialog
    if (showEditDialog && selectedExpense != null) {
        EditExpenseDialog(
            expense = selectedExpense!!,
            onDismiss = { showEditDialog = false },
            onSave = { updatedExpense ->
                viewModel.updateExpense(updatedExpense)
                showEditDialog = false
            }
        )
    }
}