package week11.st566236.finalproject.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BiometricViewModel(application: Application) : AndroidViewModel(application) {

    private val authenticator = BiometricAuthenticator(application)

    private val _canUseBiometrics = MutableLiveData<Boolean>()
    val canUseBiometrics: LiveData<Boolean> = _canUseBiometrics

    fun checkBiometrics() {
        _canUseBiometrics.value = authenticator.canAuthenticate()
    }
}
