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

class ExpenseRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    private val collectionRef = firestore.collection("expenses")

    fun getUserExpenses(userId: String): Flow<List<Expense>> = callbackFlow {
        val subscription = collectionRef
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else {
                    val list = snapshot?.documents?.mapNotNull { it.toObject(Expense::class.java)?.copy(id = it.id) } ?: emptyList()
                    trySend(list)
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun addExpense(expense: Expense, userId: String) {
        val newExpense = expense.copy(userId = userId) // include userId
        collectionRef.add(newExpense).await()
    }

    suspend fun deleteExpense(expenseId: String) {
        collectionRef.document(expenseId).delete().await()
    }

    suspend fun updateExpense(expense: Expense) {
        collectionRef.document(expense.id).set(expense).await()
    }
}