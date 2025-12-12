package week11.st566236.finalproject.data.model

// Enum class representing different categories of expenses
enum class ExpenseCategory {
    Food, Transportation, Housing, Bills, Entertainment, Shopping, Health, Other
}

// Data class representing a single expense item
data class Expense(
    val id: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val category: ExpenseCategory = ExpenseCategory.Other,
    val date: Long = System.currentTimeMillis(),
    val userId: String = ""
)