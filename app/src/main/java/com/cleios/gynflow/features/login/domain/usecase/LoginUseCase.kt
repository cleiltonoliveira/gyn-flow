package com.cleios.gynflow.features.login.domain.usecase

class LoginUseCase {

    fun login(username: String, password: String): Boolean {
        // Implement the login logic here
        // This is a placeholder implementation
        return username.isNotEmpty() && password.isNotEmpty()
    }
}