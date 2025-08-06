package com.cleios.gynflow.features.workouts

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import android.widget.Toast
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutEditScreen(
    workoutId: String,
    viewModel: WorkoutViewModel = hiltViewModel(),
    onWorkoutUpdated: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = uiState.date?.time)
    var showDatePicker by remember { mutableStateOf(false) }

    val validation by viewModel.validationState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    LaunchedEffect(workoutId) {
        viewModel.loadWorkout(workoutId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit Workout") }, navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!isSaving) {
                        viewModel.updateWorkout(
                            onSuccess = onWorkoutUpdated,
                            onError = {
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Check, contentDescription = "Salvar treino")
                }
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
                label = { Text("Nome do exercício") },
                isError = validation.nameError,
                supportingText = {
                    if (validation.nameError) Text("Nome é obrigatório")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                isError = validation.descriptionError,
                supportingText = {
                    if (validation.descriptionError) Text("Descrição é obrigatória")
                },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (validation.dateError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface
                )
            ) {
                Text(uiState.date?.let { "Data: ${dateFormatter.format(it)}" } ?: "Selecionar data")
            }

            if (validation.dateError) {
                Text(
                    text = "Data é obrigatória",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
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
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Exercícios", style = MaterialTheme.typography.titleMedium)

            uiState.exercises.forEachIndexed { index, exercise ->
                ExerciseCard(
                    exercise = exercise,
                    isNameError = validation.exerciseErrors.getOrNull(index) == true,
                    onNameChange = { viewModel.onExerciseNameChange(index, it) },
                    onObservationChange = { viewModel.onExerciseObservationChange(index, it) },
                    onImageSelected = { viewModel.onExerciseImageSelected(index, it) },
                    onRemove = { viewModel.removeExercise(index) }
                )
            }

            OutlinedButton(
                onClick = { viewModel.addExercise() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar exercício")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Adicionar exercício")
            }
        }
    }
}
