package week11.st566236.finalproject.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import week11.st566236.finalproject.data.model.Expense
import week11.st566236.finalproject.data.model.ExpenseCategory
import week11.st566236.finalproject.data.repository.ExpenseRepository
import week11.st566236.finalproject.data.util.Resource

class ExpensesViewModel(
    private val repo: ExpenseRepository = ExpenseRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getExpenses()
    }

    fun getExpenses() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repo.getUserExpenses(userId)
                .catch { _uiState.value = _uiState.value.copy(listResource = Resource.Error(it.message ?: "Error")) }
                .collect { _uiState.value = _uiState.value.copy(listResource = Resource.Success(it)) }
        }
    }

    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            try {
                repo.deleteExpense(expenseId)
            } catch (e: Exception) {
                // optionally handle delete errors
            }
        }
    }

    fun addExpense(title: String, amount: Double, category: ExpenseCategory, date: Long) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val newExpense = Expense(
            title = title,
            amount = amount,
            category = category,
            date = date,
            userId = userId
        )
        viewModelScope.launch {
            try {
                repo.addExpense(newExpense.copy(), userId)
            } catch (e: Exception) {
                // Optionally handle error
            }
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                repo.updateExpense(expense)
            } catch (e: Exception) {
                // optionally handle error, e.g., show a toast or update UI state
            }
        }
    }
}

data class ExpensesUiState(
    val listResource: Resource<List<Expense>> = Resource.Loading
)