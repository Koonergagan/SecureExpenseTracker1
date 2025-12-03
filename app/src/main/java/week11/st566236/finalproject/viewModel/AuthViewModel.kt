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

    private val _authState = MutableStateFlow<Resource<Unit>?>(null)
    val authState = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repo.login(email, password).collect { _authState.value = it }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            repo.register(email, password).collect { _authState.value = it }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            repo.resetPassword(email).collect { _authState.value = it }
        }
    }

    fun logout() = repo.logout()

    fun clearState() {
        _authState.value = null
    }
}