package week11.st566236.finalproject.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import week11.st566236.finalproject.data.model.Expense
import week11.st566236.finalproject.data.util.Resource

// Repository class to handle CRUD operations for expenses using Firebase Firestore
class ExpenseRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    // Reference to the "expenses" collection in Firestore
    private val collectionRef = firestore.collection("expenses")

    // Function to get all expenses for a specific user as a Flow
    fun getUserExpenses(userId: String): Flow<List<Expense>> = callbackFlow {
        // Listen for real-time updates in the "expenses" collection for the given user
        val subscription = collectionRef
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Close the flow if an error occurs
                    close(error)
                } else {
                    // Map Firestore documents to Expense objects and include the document ID
                    val list = snapshot?.documents?.mapNotNull { it.toObject(Expense::class.java)?.copy(id = it.id) } ?: emptyList()
                    trySend(list) // Send the updated list to the flow
                }
            }

        // Clean up the listener when the flow is closed
        awaitClose { subscription.remove() }
    }

    // Add a new expense for a specific user
    suspend fun addExpense(expense: Expense, userId: String) {
        val newExpense = expense.copy(userId = userId) // include userId
        collectionRef.add(newExpense).await()
    }

    // Delete an expense by its document ID
    suspend fun deleteExpense(expenseId: String) {
        collectionRef.document(expenseId).delete().await()
    }

    // Update an existing expense by its document ID
    suspend fun updateExpense(expense: Expense) {
        collectionRef.document(expense.id).set(expense).await()
    }
}