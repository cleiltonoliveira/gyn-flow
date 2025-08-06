package com.cleios.gynflow.features.login.presentation

import com.cleios.gynflow.core.auth.CustomAuthService
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: CustomAuthService
) : ViewModel() {

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun login(email: String, password: String, onSuccess: () -> Unit) {
            authService.login(email, password) { success, error ->
            if (success) {
                errorMessage = null
                onSuccess()
            } else {
                errorMessage = error
            }
        }
    }

    fun register(email: String, password: String, onSuccess: () -> Unit) {
        authService.register(email, password) { success, error ->
            if (success) {
                errorMessage = null
                onSuccess()
            } else {
                errorMessage = error
            }
        }
    }
}
