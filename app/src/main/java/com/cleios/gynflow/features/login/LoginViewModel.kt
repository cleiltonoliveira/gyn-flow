package com.cleios.gynflow.features.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cleios.gynflow.core.auth.CustomAuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: CustomAuthService
) : ViewModel() {

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        isLoading = true
        authService.login(email, password) { success, error ->
            isLoading = false
            if (success) {
                errorMessage = null
                onSuccess()
            } else {
                errorMessage = error
            }
        }
    }

    fun register(email: String, password: String, onSuccess: () -> Unit) {
        isLoading = true
        authService.register(email, password) { success, error ->
            isLoading = false
            if (success) {
                errorMessage = null
                onSuccess()
            } else {
                errorMessage = error
            }
        }
    }

    fun setError(message: String) {
        errorMessage = message
    }

    fun clearError() {
        errorMessage = null
    }
}
