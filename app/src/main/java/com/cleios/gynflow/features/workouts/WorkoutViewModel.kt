package com.cleios.gynflow.features.workouts

import android.net.Uri
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
    private val _uiState = MutableStateFlow(Workout())
    val uiState: StateFlow<Workout> = _uiState.asStateFlow()

    private val _validationState = MutableStateFlow(WorkoutFormValidation())
    val validationState: StateFlow<WorkoutFormValidation> = _validationState.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

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

    fun onExerciseImageSelected(index: Int, uri: Uri) {
        val updated = _uiState.value.exercises.toMutableList()
        if (index in updated.indices) {
            updated[index] = updated[index].copy(localImageUri = uri)
            _uiState.update { it.copy(exercises = updated) }
        }
    }

    fun saveWorkout(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                if (!validateForm()) return@launch
                repository.addWorkout(buildWorkoutFromState())
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao salvar o treino")
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun loadWorkout(id: String) {
        viewModelScope.launch {
            repository.getWorkoutById(id) { workout ->
                _uiState.update {
                    it.copy(
                        id = workout?.id ?: "",
                        name = workout?.name ?: "",
                        description = workout?.description ?: "",
                        date = workout?.date,
                        exercises = workout?.exercises ?: emptyList()
                    )
                }
            }
        }
    }

    fun updateWorkout(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isSaving.value = true
            if (!validateForm()) return@launch
            try {
                repository.updateWorkout(buildWorkoutFromState())
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Erro ao salvar o treino")
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun buildWorkoutFromState(): Workout {
        return Workout(
            id = _uiState.value.id,
            name = _uiState.value.name,
            description = _uiState.value.description,
            date = _uiState.value.date,
            exercises = _uiState.value.exercises
        )
    }

    private fun validateForm(): Boolean {
        val nameError = _uiState.value.name.isBlank()
        val descriptionError = _uiState.value.description.isBlank()
        val dateError = _uiState.value.date == null
        val exerciseErrors = _uiState.value.exercises.map { it.name.isBlank() }

        _validationState.value = WorkoutFormValidation(
            nameError = nameError,
            descriptionError = descriptionError,
            dateError = dateError,
            exerciseErrors = exerciseErrors
        )

        return !nameError && !descriptionError && !dateError && exerciseErrors.all { !it }
    }
}

