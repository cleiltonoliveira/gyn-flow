package com.cleios.gynflow.app

import com.cleios.gynflow.core.auth.CustomAuthService
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class AppViewModel @Inject constructor(
    private val authService: CustomAuthService
) : ViewModel() {


    private val _isLoggedIn = MutableStateFlow(authService.currentUser != null)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        authService.  firebaseAuth?.addAuthStateListener {
            _isLoggedIn.value = it.currentUser != null
        }
    }
    fun logout() {
        authService.logout()
    }

    fun isLoggedIn(): Boolean = authService.currentUser != null
}