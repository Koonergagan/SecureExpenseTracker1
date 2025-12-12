package week11.st566236.finalproject.ui.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st566236.finalproject.data.model.Expense
import week11.st566236.finalproject.data.util.Resource
import week11.st566236.finalproject.viewModel.ExpensesViewModel
import androidx.compose.ui.geometry.Offset

/**
 * Screen displaying the user's expenses with options to add, edit, or delete.
 *
 */
@Composable
fun ExpensesScreen(
    viewModel: ExpensesViewModel = viewModel()
) {
    // Collect UI state from ViewModel
    val uiState by viewModel.uiState.collectAsState()
    // Dialog state flags
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    // Currently selected expense for editing
    var selectedExpense by remember { mutableStateOf<Expense?>(null) }

    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.surface
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Handle loading, error, and success states
        when (val resource = uiState.listResource) {
            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is Resource.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Error: ${resource.message}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            is Resource.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Display each expense item
                    items(resource.data) { expense ->
                        ExpenseItemRow(
                            expense = expense,
                            onEdit = {
                                selectedExpense = expense
                                showEditDialog = true
                            },
                            onDelete = { viewModel.deleteExpense(it) }
                        )
                    }
                }
            }
        }

        // Floating Action Button for adding a new expense
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(20.dp),
            elevation = FloatingActionButtonDefaults.elevation(10.dp)
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
