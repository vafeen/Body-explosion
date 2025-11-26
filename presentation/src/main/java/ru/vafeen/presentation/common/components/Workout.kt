package ru.vafeen.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vafeen.domain.models.Workout
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.utils.formatAsLocalDateTime
import ru.vafeen.presentation.common.utils.formatAsTime
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize

/**
 * Composable-функция, которая отображает информацию о тренировке [Workout] в виде карточки.
 */
@Composable
internal fun Workout.WorkoutString() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.buttonColor,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.padding(5.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextForThisTheme(
                    text = stringResource(R.string.date_time),
                    fontSize = FontSize.small17
                )
                TextForThisTheme(
                    text = this@WorkoutString.dateTime.formatAsLocalDateTime(seconds = false),
                    fontSize = FontSize.small17
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextForThisTheme(
                    text = stringResource(R.string.duration),
                    fontSize = FontSize.small17
                )
                TextForThisTheme(
                    text = this@WorkoutString.duration.formatAsTime(hours = false),
                    fontSize = FontSize.small17
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextForThisTheme(
                    text = stringResource(R.string.count_of_exercises),
                    fontSize = FontSize.small17
                )
                TextForThisTheme(
                    text = "${this@WorkoutString.countOfExercises}",
                    fontSize = FontSize.small17
                )
            }
        }
    }
}
