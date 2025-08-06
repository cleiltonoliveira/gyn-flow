package com.cleios.gynflow.app

import com.cleios.gynflow.core.auth.CustomAuthService
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.google.firebase.auth.FirebaseUser

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: CustomAuthService
) : ViewModel() {

    fun isLoggedIn(): Boolean = authService.currentUser!= null
    fun logout(){
        authService.logout()
    }
    val user: FirebaseUser?
        get() = authService.currentUser

}