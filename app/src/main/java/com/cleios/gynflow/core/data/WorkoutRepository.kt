package com.cleios.gynflow.core.data

import android.net.Uri
import com.cleios.gynflow.core.auth.CustomAuthService
import com.cleios.gynflow.core.model.Workout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: CustomAuthService
) {
    fun getWorkouts(onResult: (List<Workout>) -> Unit) {
        val user = auth.currentUser
        val path = "users/${user?.uid}/workouts"

        db.collection(path)
            .get()
            .addOnSuccessListener { snapshot ->
                val workouts = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Workout::class.java)?.copy(id = doc.id)
                }
                onResult(workouts)
            }
    }

    suspend fun addWorkout(workout: Workout) {
        val user = auth.currentUser ?: return
        val path = "users/${user.uid}/workouts"
        val doc = db.collection(path).document()

        val updatedExercises = workout.exercises.map { exercise ->
            val uri = exercise.localImageUri
            val uploadedUrl = uri?.let { uploadImageToFirebase(it) } ?: exercise.imageUrl
            exercise.copy(imageUrl = uploadedUrl, localImageUri = null)
        }

        val workoutWithUploadedImages = workout.copy(exercises = updatedExercises)
        doc.set(workoutWithUploadedImages).await()
    }


    private suspend fun uploadImageToFirebase(uri: Uri): String = withContext(Dispatchers.IO) {
        val user = auth.currentUser
        val storageRef = storage.reference
        val imageRef =
            storageRef.child("accounts/${user?.uid}/${System.currentTimeMillis()}_${uri.lastPathSegment}")
        val uploadTask = imageRef.putFile(uri).await()
        return@withContext imageRef.downloadUrl.await().toString()
    }

    suspend fun deleteWorkout(workoutId: String) {
        val user = auth.currentUser
        val path = "users/${user?.uid}/workouts"
        db.collection(path).document(workoutId).delete().await()
    }

}