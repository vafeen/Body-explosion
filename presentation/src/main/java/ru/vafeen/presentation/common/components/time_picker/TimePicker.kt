package ru.vafeen.presentation.common.components.time_picker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.ui.theme.FontSize
import java.time.LocalTime


@Composable
internal fun TimePicker(
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextForThisTheme(
                text = stringResource(R.string.minutes),
                fontSize = FontSize.medium19
            )
            TimeColumnPicker(

                initialValue = initialTime.minute,
                onValueChange = { minute ->
                    onTimeSelected(initialTime.withMinute(minute))
                },
                range = 0..99,
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextForThisTheme(
                text = stringResource(R.string.seconds),
                fontSize = FontSize.medium19
            )
            TimeColumnPicker(
                initialValue = initialTime.second,
                onValueChange = { second ->
                    onTimeSelected(initialTime.withSecond(second))
                },
                range = 0..59,
            )
        }
    }
}