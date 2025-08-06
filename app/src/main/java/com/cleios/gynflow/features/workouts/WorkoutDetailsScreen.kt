package com.cleios.gynflow.features.workouts

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.filled.Edit
import coil.compose.AsyncImage
import com.cleios.gynflow.core.model.ExerciseInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailsScreen(
    workoutId: String,
    viewModel: WorkoutViewModel = hiltViewModel(),
    onEditClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    LaunchedEffect(workoutId) {
        viewModel.loadWorkout(workoutId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout Details") },
                actions = {
                    IconButton(onClick = { onEditClick(workoutId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Workout")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                uiState.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            DetailSection(title = "Description", content = uiState.description)
            DetailSection(
                title = "Date",
                content = uiState.date?.let { dateFormatter.format(it) } ?: "Not selected"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Exercises",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.exercises.isEmpty()) {
                Text("No exercises added.", style = MaterialTheme.typography.bodyMedium)
            } else {
                uiState.exercises.forEach { exercise ->
                    ReadOnlyExerciseCard(exercise)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun DetailSection(title: String, content: String) {
    Column(Modifier.padding(bottom = 16.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            content,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun ReadOnlyExerciseCard(exercise: ExerciseInput) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                exercise.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (exercise.observations.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Observations",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    exercise.observations,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (exercise.imageUrl != null) {
                Spacer(modifier = Modifier.height(12.dp))
                AsyncImage(
                    model = exercise.imageUrl,
                    contentDescription = "Exercise Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                )
            }
        }
    }
}
