package com.cleios.gynflow.features.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit,
                viewModel: LoginViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val error = viewModel.errorMessage

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        error?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
        }
        Button(
            onClick = { viewModel.login(email, password, onLoginSuccess) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Entrar")
        }

        Button(
            onClick = { viewModel.register(email, password, onLoginSuccess) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Criar conta")
        }
    }
}
