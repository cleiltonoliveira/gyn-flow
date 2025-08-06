package com.cleios.gynflow.features.workouts

data class WorkoutFormValidation(
    val nameError: Boolean = false,
    val descriptionError: Boolean = false,
    val dateError: Boolean = false,
    val exerciseErrors: List<Boolean> = emptyList()
)