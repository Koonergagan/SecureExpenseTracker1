package week11.st566236.finalproject.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import week11.st566236.finalproject.data.util.Resource

// Repository class to handle user authentication with FirebaseAuth
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    // Login function
    fun login(email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Login failed"))
        }
    }

    // Register function
    fun register(email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Register failed"))
        }
    }

    // Password reset function
    fun resetPassword(email: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to send reset email"))
        }
    }

    // Logout function
    fun logout() {
        auth.signOut()
    }

    // Check if a user is currently logged in
    fun isUserLoggedIn(): Boolean =
        auth.currentUser != null
}