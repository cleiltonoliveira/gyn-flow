package com.cleios.gynflow.features.login.presentation

import com.cleios.gynflow.features.login.domain.model.User

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null
)