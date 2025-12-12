package week11.st566236.finalproject.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import week11.st566236.finalproject.data.repository.AuthRepository
import week11.st566236.finalproject.data.util.Resource

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    // Tracks the authentication operation state (loading, success, error)
    private val _authState = MutableStateFlow<Resource<Unit>?>(null)
    val authState = _authState.asStateFlow()

    /**
     * Attempts to log in with the provided email and password.
     * Collects the Resource<Unit> emitted by the repository and exposes it to UI.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            repo.login(email, password).collect { _authState.value = it }
        }
    }

    /**
     * Registers a new user with email and password.
     */
    fun register(email: String, password: String) {
        viewModelScope.launch {
            repo.register(email, password).collect { _authState.value = it }
        }
    }

    /**
     * Sends a password reset email.
     */
    fun resetPassword(email: String) {
        viewModelScope.launch {
            repo.resetPassword(email).collect { _authState.value = it }
        }
    }

    /**
     * Logs out the current user.
     * This is synchronous because FirebaseAuth handles logout locally.
     */
    fun logout() = repo.logout()

    /**
     * Resets authState (typically when navigating away or after showing toast/snackbar).
     */
    fun clearState() {
        _authState.value = null
    }
}