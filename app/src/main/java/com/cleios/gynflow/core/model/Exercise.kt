package com.cleios.gynflow.core.model

import android.net.Uri

data class ExerciseInput(
    val name: String = "",
    val imageUrl: String = "",
    val localImageUri: Uri? = null,
    val observations: String = ""
)