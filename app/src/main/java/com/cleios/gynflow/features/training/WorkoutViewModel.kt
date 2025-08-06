package com.cleios.gynflow.features.training

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleios.gynflow.core.data.WorkoutRepository
import com.cleios.gynflow.core.model.ExerciseInput
import com.cleios.gynflow.core.model.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {

    var name by mutableStateOf("")
    var description by mutableStateOf("")

    var isSaving by mutableStateOf(false)
    var saveSuccess by mutableStateOf<Boolean?>(null)

    fun saveWorkout(workout: Workout) {
        viewModelScope.launch {
            isSaving = true
            try {
                repository.addWorkout(workout)
                saveSuccess = true
            } catch (e: Exception) {
                saveSuccess = false
            }
            isSaving = false
        }
    }

    fun removerTreino(treino: Workout) {
        viewModelScope.launch {
            repository.deleteWorkout(treino.id)
            loadWorkouts()
        }
    }

    private val _treinos = mutableStateOf<List<Workout>>(emptyList())
    val treinos: State<List<Workout>> get() = _treinos

    fun loadWorkouts() {
        repository.getWorkouts {
            _treinos.value = it
        }
    }


    private val _uiState = MutableStateFlow(Workout())
    val uiState: StateFlow<Workout> = _uiState.asStateFlow()


    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value) }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value) }
    }

    fun onDateChange(value: Date) {
        _uiState.update { it.copy(date = value) }
    }

    fun addExercise() {
        val updatedExercises = _uiState.value.exercises.toMutableList()
        updatedExercises.add(ExerciseInput())
        _uiState.update { it.copy(exercises = updatedExercises) }
    }

    fun removeExercise(index: Int) {
        val updatedExercises = _uiState.value.exercises.toMutableList()
        if (index in updatedExercises.indices) {
            updatedExercises.removeAt(index)
            _uiState.update { it.copy(exercises = updatedExercises) }
        }
    }

    fun onExerciseNameChange(index: Int, value: String) {
        val updatedExercises = _uiState.value.exercises.toMutableList()
        if (index in updatedExercises.indices) {
            updatedExercises[index] = updatedExercises[index].copy(name = value)
            _uiState.update { it.copy(exercises = updatedExercises) }
        }
    }

    fun onExerciseObservationChange(index: Int, value: String) {
        val updatedExercises = _uiState.value.exercises.toMutableList()
        if (index in updatedExercises.indices) {
            updatedExercises[index] = updatedExercises[index].copy(observations = value)
            _uiState.update { it.copy(exercises = updatedExercises) }
        }
    }

    fun onExerciseImageUrlChange(index: Int, value: String) {
        val updatedExercises = _uiState.value.exercises.toMutableList()
        if (index in updatedExercises.indices) {
            updatedExercises[index] = updatedExercises[index].copy(imageUrl = value)
            _uiState.update { it.copy(exercises = updatedExercises) }
        }
    }

    fun saveWorkout(onSuccess: () -> Unit, onError: (String) -> Unit) {

        viewModelScope.launch {
            isSaving = true
            try {
                repository.addWorkout(
                    Workout(
                        name = _uiState.value.name,
                        description = _uiState.value.description,
                        date = _uiState.value.date,
                        exercises = _uiState.value.exercises
                    )
                )
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao salvar o treino")
            }
        }

    }

}
