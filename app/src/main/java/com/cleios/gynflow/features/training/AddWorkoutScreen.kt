package com.cleios.gynflow.features.training

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cleios.gynflow.core.model.ExerciseInput
import com.cleios.gynflow.core.model.Workout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddWorkoutScreen(
//    viewModel: WorkoutViewModel = hiltViewModel(),
//    onSave: () -> Unit
//) {
//    var workoutName by remember { mutableStateOf("") }
//    var workoutDescription by remember { mutableStateOf("") }
//
//    var exercises by remember { mutableStateOf(mutableListOf<Exercise>()) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("Novo Treino") })
//        },
//    ) { paddingValues ->
//        Column(modifier = Modifier.padding(paddingValues)) {
//            Text("Add Workout", fontSize = 20.sp, fontWeight = FontWeight.Bold)
//
//            OutlinedTextField(
//                value = workoutName,
//                onValueChange = { workoutName = it },
//                label = { Text("Workout Name") }
//            )
//
//            OutlinedTextField(
//                value = workoutDescription,
//                onValueChange = { workoutDescription = it },
//                label = { Text("Workout Description") }
//            )
//
//            Spacer(Modifier.height(16.dp))
//            Text("Exercises", fontWeight = FontWeight.Bold)
//
//            exercises.forEachIndexed { index, exercise ->
//                Card(Modifier.padding(vertical = 4.dp)) {
//                    Column(Modifier.padding(8.dp)) {
//                        Text("Exercise ${index + 1}: ${exercise.name}")
//                        Text("Notes: ${exercise.notes}")
//                    }
//                }
//            }
//
//            Button(onClick = {
//                exercises.add(Exercise(name = "", notes = ""))
//            }) {
//                Text("Add Exercise")
//            }
//
//            Button(
//                onClick = {
//                    val workout = Workout(
//                        name = workoutName,
//                        description = workoutDescription,
//                        exercises = exercises
//                    )
//                    viewModel.saveWorkout(workout)
//                    onSave()
//                },
//                modifier = Modifier.padding(top = 16.dp)
//            ) {
//                Text("Save Workout")
//            }
//        }
//
//
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(
    viewModel: WorkoutViewModel = hiltViewModel(),
    onWorkoutSaved: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = uiState.date?.time)
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("New Workout") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.saveWorkout(
                    onSuccess = onWorkoutSaved,
                    onError = {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                )
            }) {
                Icon(Icons.Default.Check, contentDescription = "Save")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Workout name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(uiState.date?.let { "Date: ${dateFormatter.format(it)}" } ?: "Select date")
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    viewModel.onDateChange(Date(it))
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Exercises", style = MaterialTheme.typography.titleMedium)

            uiState.exercises.forEachIndexed { index, exercise ->
                ExerciseCard(
                    exercise = exercise,
                    onNameChange = { viewModel.onExerciseNameChange(index, it) },
                    onObservationChange = { viewModel.onExerciseObservationChange(index, it) },
                    onImageUrlChange = { viewModel.onExerciseImageUrlChange(index, it) },
                    onRemove = { viewModel.removeExercise(index) }
                )
            }

            OutlinedButton(
                onClick = { viewModel.addExercise() },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Exercise")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Exercise")
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: ExerciseInput,
    onNameChange: (String) -> Unit,
    onObservationChange: (String) -> Unit,
    onImageUrlChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = exercise.name,
                onValueChange = onNameChange,
                label = { Text("Exercise name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = exercise.observations,
                onValueChange = onObservationChange,
                label = { Text("Observations") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = exercise.imageUrl,
                onValueChange = onImageUrlChange,
                label = { Text("Image URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Remove")
                }
            }
        }
    }
}
