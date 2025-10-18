package ru.vafeen.presentation.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vafeen.domain.models.Training
import ru.vafeen.presentation.ui.theme.FontSize


@Composable
internal fun Training.TrainingString(
    updateTraining: (Training) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Checkbox(checked = this@TrainingString.isIncludedToTraining, onCheckedChange = {
            updateTraining(this@TrainingString.copy(isIncludedToTraining = it))
        })
        TextForThisTheme(text = "${this@TrainingString.name}", fontSize = FontSize.small17)
    }
}