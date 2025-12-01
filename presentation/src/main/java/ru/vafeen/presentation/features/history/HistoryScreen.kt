package ru.vafeen.presentation.features.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.vafeen.domain.models.Workout
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.common.components.WorkoutString
import ru.vafeen.presentation.ui.theme.FontSize
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Экран, отображающий историю тренировок.
 *
 * @param viewModel ViewModel для экрана истории.
 */
@Composable
internal fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
//    val state by viewModel.state.collectAsState(HistoryState())
    val state = HistoryState(
        workout = listOf(
            Workout(
                text = "Утренняя йога",
                dateTime = LocalDateTime.of(2025, 10, 5, 7, 30),
                duration = LocalTime.of(0, 30, 0),
                countOfExercises = 12
            ),
            Workout(
                text = "Силовая тренировка",
                dateTime = LocalDateTime.of(2025, 10, 12, 18, 15),
                duration = LocalTime.of(0, 45, 20),
                countOfExercises = 8
            ),
            Workout(
                text = "Кардио-тренировка",
                dateTime = LocalDateTime.of(2025, 10, 19, 8, 0),
                duration = LocalTime.of(1, 0, 15),
                countOfExercises = 10
            ),
            Workout(
                text = "Стретчинг",
                dateTime = LocalDateTime.of(2025, 10, 26, 19, 0),
                duration = LocalTime.of(0, 25, 45),
                countOfExercises = 6
            ),
            Workout(
                text = "Кроссфит",
                dateTime = LocalDateTime.of(2025, 11, 2, 9, 30),
                duration = LocalTime.of(1, 15, 30),
                countOfExercises = 15
            ),
            Workout(
                text = "Медитативная йога",
                dateTime = LocalDateTime.of(2025, 11, 9, 7, 0),
                duration = LocalTime.of(0, 40, 0),
                countOfExercises = 7
            ),
            Workout(
                text = "Функциональный тренинг",
                dateTime = LocalDateTime.of(2025, 11, 16, 17, 45),
                duration = LocalTime.of(0, 50, 10),
                countOfExercises = 9
            ),
            Workout(
                text = "Аштанга йога",
                dateTime = LocalDateTime.of(2025, 11, 23, 8, 15),
                duration = LocalTime.of(1, 10, 25),
                countOfExercises = 14
            ),
            Workout(
                text = "Силовая тренировка",
                dateTime = LocalDateTime.of(2025, 11, 30, 18, 30),
                duration = LocalTime.of(0, 55, 40),
                countOfExercises = 11
            ),
            Workout(
                text = "Кардио-тренировка",
                dateTime = LocalDateTime.of(2025, 12, 7, 10, 0),
                duration = LocalTime.of(1, 5, 0),
                countOfExercises = 13
            ),
            Workout(
                text = "Хатха йога",
                dateTime = LocalDateTime.of(2025, 12, 14, 7, 45),
                duration = LocalTime.of(0, 35, 20),
                countOfExercises = 8
            ),
            Workout(
                text = "Кроссфит",
                dateTime = LocalDateTime.of(2025, 12, 21, 19, 15),
                duration = LocalTime.of(0, 48, 30),
                countOfExercises = 10
            ),
            Workout(
                text = "Виньяса йога",
                dateTime = LocalDateTime.of(2025, 12, 28, 8, 30),
                duration = LocalTime.of(1, 20, 0),
                countOfExercises = 16
            )
        ).sortedByDescending { it.dateTime }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (state.workout.isEmpty()) Arrangement.spacedBy(20.dp)
        else Arrangement.Center
    ) {
        TextForThisTheme(
            text = "History", fontSize = FontSize.big22,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        if (state.workout.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier)
                }
                items(state.workout) { workout ->
                    workout.WorkoutString()
                }
                item {
                    Spacer(modifier = Modifier)
                }
            }

        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                TextForThisTheme(text = "Тренировок еще не было", fontSize = FontSize.medium19)
            }
        }
    }
}