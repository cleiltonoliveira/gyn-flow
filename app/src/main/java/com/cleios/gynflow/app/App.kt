package com.cleios.gynflow.app

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cleios.gynflow.features.home.HomeScreen
import com.cleios.gynflow.features.login.LoginScreen
import com.cleios.gynflow.features.training.AddWorkoutScreen

@Composable
fun GynFlowApp(
    navController: NavHostController = rememberNavController(),
    viewModel: AuthViewModel = hiltViewModel()
) {
    val startDestination = if (viewModel.isLoggedIn()) "home" else "login"

    NavHost(navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("home") },
            )
        }
        composable("home") {
            HomeScreen(
                onWorkoutClick = { treino ->
                    navController.navigate("treino/${treino.id}")
                },
                onAddClick = { navController.navigate("addWorkout") },
            )
        }
        composable("addWorkout") {
            AddWorkoutScreen(
                onWorkoutSaved = { navController.popBackStack() }
            )
        }
    }

}