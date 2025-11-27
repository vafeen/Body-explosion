package ru.vafeen.presentation.features.time_picker_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.common.components.time_picker.TimePicker
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize
import java.time.LocalTime

/**
 * Диалог выбора времени.
 *
 * Компонент отображает окно с выбором времени и двумя кнопками — "Отмена" и "Подтвердить".
 * Работает по MVI-подходу через [TimePickerViewModel].
 *
 * @param onTimeSelected Колбэк, вызываемый при подтверждении выбранного времени.
 * @param onDismissRequest Колбэк, вызываемый при закрытии диалога без подтверждения.
 */
@Composable
internal fun TimePickerDialog(
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val viewModel: TimePickerViewModel =
        hiltViewModel<TimePickerViewModel, TimePickerViewModel.Factory>(
            creationCallback = {
                it.create(
                    initialTime = initialTime,
                    onTimeSelected = onTimeSelected,
                    onDismissRequest = onDismissRequest
                )
            }
        )
    val state by viewModel.state.collectAsState()
    Dialog(
        onDismissRequest = {
            viewModel.handleIntent(TimePickerIntent.Cancel)
            onDismissRequest()
        },
    ) {
        Column(modifier = Modifier.background(AppTheme.colors.background)) {
            TimePicker(
                initialTime = state.currentTime,
                onTimeSelected = { viewModel.handleIntent(TimePickerIntent.UpdateTime(it)) },
            )
            Row {
                Button(onClick = {
                    viewModel.handleIntent(TimePickerIntent.Cancel)
                    onDismissRequest()
                }) {
                    TextForThisTheme(
                        text = "cancel",
                        fontSize = FontSize.medium19
                    )
                }
                Button(onClick = {
                    viewModel.handleIntent(TimePickerIntent.Confirm)
                    onTimeSelected(state.currentTime)
                    onDismissRequest()
                }) {
                    TextForThisTheme(
                        text = "confirm",
                        fontSize = FontSize.medium19
                    )
                }
            }
        }
    }
}
