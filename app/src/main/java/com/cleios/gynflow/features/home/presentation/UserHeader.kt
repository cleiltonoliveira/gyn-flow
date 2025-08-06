package com.cleios.gynflow.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun UserHeader(
    userName: String,
    onSignOff: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar Circle
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
        ) {
            Text(
                text = userName.firstOrNull()?.toString() ?: "",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        // User Name
        Text(
            text = userName,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium
        )
        // Signoff Button
        Button(onClick = onSignOff) {
            Text("Sign Off")
        }
    }
}