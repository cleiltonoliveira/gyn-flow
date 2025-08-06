package com.cleios.gynflow.features.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.cleios.gynflow.core.model.Workout
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onAddClick: () -> Unit,
    onWorkoutClick: (Workout) -> Unit
) {
    val workouts = viewModel.workouts

    LaunchedEffect(Unit) {
        viewModel.loadWorkouts()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Meus Treinos") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Novo treino")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            UserHeader(
                userName = viewModel.user?.email.orEmpty(),
                onSignOff = { viewModel.logout() }
            )
            LazyColumn {
                items(workouts.value, key = { it.id }) { workout ->
                    SwipeToDeleteItem(
                        workout = workout,
                        onDelete = { viewModel.removeWorkout(it) },
                        onClick = { onWorkoutClick(workout) }
                    )
                }
            }
        }
    }
}

@Composable
fun SwipeToDeleteItem(
    workout: Workout,
    onDelete: (Workout) -> Unit,
    onClick: () -> Unit
) {
    val swipeThreshold = 150f
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX, label = "")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer)
            .height(IntrinsicSize.Min)
    ) {
        // Background Delete Icon
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 24.dp)
        )
        Card(
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .fillMaxWidth()
                .padding(8.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX < -swipeThreshold) {
                                onDelete(workout)
                            } else {
                                offsetX = 0f
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            offsetX = (offsetX + dragAmount).coerceAtMost(0f)
                        }
                    )
                }
                .clickable { onClick() },
            elevation = CardDefaults.cardElevation()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(workout.name, fontWeight = FontWeight.Bold)
                Text(workout.description)
            }
        }
    }
}



