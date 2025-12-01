package ru.vafeen.presentation.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.components.time_picker.TimePicker
import ru.vafeen.presentation.ui.theme.AppTheme
import java.time.LocalTime


@Composable
internal fun SettingsTabWithTimePicker(
    time: LocalTime,
    description: String,
    timeToChangeInDialog: LocalTime,
    isTimePickerShowed: Boolean,
    switchTimePickerIsShowed: () -> Unit,
    updateTime: (LocalTime) -> Unit,
    applyTime: () -> Unit,
    resetTime: () -> Unit,
) {
//    CardOfSettings(
//        text = time.formatAsTime(),
//        icon = {
//            Icon(
//                painter = painterResource(R.drawable.schedule),
//                contentDescription = description
//            )
//        },
//        onClick = switchTimePickerIsShowed,
//    )
    if (isTimePickerShowed) {
        Dialog(
            onDismissRequest = if (time == timeToChangeInDialog) switchTimePickerIsShowed else {
                {}
            }
        ) {
            Card(
                modifier = Modifier,
                border = BorderStroke(1.dp, AppTheme.colors.text)
            ) {
                TimePicker(initialTime = timeToChangeInDialog, updateTime)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = switchTimePickerIsShowed) {
                        Icon(
                            painter = painterResource(R.drawable.close),
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                    IconButton(onClick = resetTime) {
                        Icon(
                            painter = painterResource(R.drawable.reset_settings),
                            contentDescription = stringResource(R.string.reset)
                        )
                    }
                    IconButton(onClick = applyTime) {
                        Icon(
                            painter = painterResource(R.drawable.check),
                            contentDescription = stringResource(R.string.apply)
                        )
                    }
                }
            }
        }
    }
}