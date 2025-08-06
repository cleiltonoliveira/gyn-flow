package com.cleios.gynflow.core.data

import com.cleios.gynflow.core.auth.CustomAuthService
import com.cleios.gynflow.core.model.Workout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor(
    private val db: FirebaseFirestore,
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
        val user = auth.currentUser
        val path = "users/${user?.uid}/workouts"
        val doc = db.collection(path).document();
        doc.set(workout).await()
    }

    suspend fun deleteWorkout(workoutId: String) {
        val user = auth.currentUser
        val path = "users/${user?.uid}/workouts"
        db.collection(path).document(workoutId).delete().await()
    }
}