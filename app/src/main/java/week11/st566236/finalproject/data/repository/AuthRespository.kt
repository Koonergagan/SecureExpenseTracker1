package week11.st566236.finalproject.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import week11.st566236.finalproject.data.util.Resource

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    fun login(email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Login failed"))
        }
    }

    fun register(email: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Register failed"))
        }
    }

    fun resetPassword(email: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to send reset email"))
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun isUserLoggedIn(): Boolean =
        auth.currentUser != null
}