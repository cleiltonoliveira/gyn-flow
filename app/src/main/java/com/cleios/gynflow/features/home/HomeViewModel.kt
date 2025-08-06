package com.cleios.gynflow.features.home


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.cleios.gynflow.core.auth.CustomAuthService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleios.gynflow.core.data.WorkoutRepository
import com.cleios.gynflow.core.model.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authService: CustomAuthService,
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _workouts = mutableStateOf<List<Workout>>(emptyList())
    val workouts: State<List<Workout>> get() = _workouts

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun logout() {
        authService.logout()
    }

    fun loadWorkouts() {
        _isLoading.value = true
        repository.getWorkouts {
            _workouts.value = it
            _isLoading.value = false
        }
    }

    fun removeWorkout(workout: Workout) {
        viewModelScope.launch {
            repository.deleteWorkout(workout.id)
            loadWorkouts()
        }
    }


    val user: FirebaseUser?
        get() = authService.currentUser

}