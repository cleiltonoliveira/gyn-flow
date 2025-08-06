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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import java.text.SimpleDateFormat
import java.util.Locale
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
        topBar = {
            TopAppBar(
                title = { Text("Meus Treinos") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Novo treino")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            UserHeader(
                userName = viewModel.user?.email.orEmpty(),
                onSignOff = { viewModel.logout() }
            )

            if (workouts.value.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Nenhum treino ainda. Toque '+' para criar um.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(8.dp)) {
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
}

@Composable
fun SwipeToDeleteItem(
    workout: Workout,
    onDelete: (Workout) -> Unit,
    onClick: () -> Unit
) {
    val swipeThreshold = 120f
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX, label = "")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(end = 24.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
        }

        Card(
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
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
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = workout.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = workout.date?.let {
                        "📅 ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it)}"
                    } ?: "Sem data",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

