package com.cleios.gynflow.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cleios.gynflow.features.home.HomeScreen
import com.cleios.gynflow.features.login.LoginScreen
import com.cleios.gynflow.features.workouts.AddWorkoutScreen
import com.cleios.gynflow.features.workouts.WorkoutDetailsScreen
import com.cleios.gynflow.features.workouts.WorkoutEditScreen

@Composable
fun GynFlowApp(
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel = hiltViewModel()
) {

    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val startDestination = if (viewModel.isLoggedIn()) "home" else "login"

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate("login") {
                popUpTo(0) // clear all backstack
            }
        }
    }

    NavHost(navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("home") },
            )
        }

        composable("home") {
            HomeScreen(
                onWorkoutClick = { workout ->
                    navController.navigate("workoutDetails/${workout.id}")
                },
                onAddClick = { navController.navigate("addWorkout") },
            )
        }

        composable("addWorkout") {
            AddWorkoutScreen(
                onWorkoutSaved = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("workoutDetails/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            WorkoutDetailsScreen(
                workoutId = id,
                onEditClick = { id -> navController.navigate("workoutEdit/${id}") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("workoutEdit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            WorkoutEditScreen(
                workoutId = id,
                onWorkoutUpdated = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}