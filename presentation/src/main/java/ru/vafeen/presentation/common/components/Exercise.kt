package ru.vafeen.presentation.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import ru.vafeen.domain.models.Exercise
import ru.vafeen.presentation.ui.theme.FontSize

/**
 * Composable-функция, которая отображает упражнение [Exercise] в виде строки с чекбоксом.
 *
 * @param updateTraining Лямбда-функция, вызываемая при изменении состояния чекбокса (включено ли упражнение в тренировку).
 */
@Composable
internal fun Exercise.ExerciseString(
    updateTraining: (Exercise) -> Unit
) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = this@ExerciseString.isIncludedToTraining, onCheckedChange = {
            updateTraining(this@ExerciseString.copy(isIncludedToTraining = it))
        })
        TextForThisTheme(text = "${this@ExerciseString.name}", fontSize = FontSize.small17)
    }
}


/**
 * Composable-функция, которая отображает упражнение [Exercise] в виде строки с чекбоксом.
 *
 * @param updateTraining Лямбда-функция, вызываемая при изменении состояния чекбокса (включено ли упражнение в тренировку).
 */
@Composable
internal fun Exercise.ExerciseString2(
    updateTraining: (Exercise) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = this@ExerciseString2.isIncludedToTraining, onCheckedChange = {
            updateTraining(this@ExerciseString2.copy(isIncludedToTraining = it))
        })
        Column {
            TextForThisTheme(
                text = "${this@ExerciseString2.name}",
                fontSize = FontSize.small17,
                fontWeight = FontWeight.Bold
            )
            TextForThisTheme(text = "Длительность: 1:00", fontSize = FontSize.small17)
        }
    }
}
