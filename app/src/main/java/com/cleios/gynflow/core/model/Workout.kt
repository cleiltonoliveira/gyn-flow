package com.cleios.gynflow.core.model

import java.util.Date

data class Workout(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val date: Date? = null,
    val exercises: List<ExerciseInput> = emptyList()
)