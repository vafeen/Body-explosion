package ru.vafeen.presentation.features.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.components.ExerciseString
import ru.vafeen.presentation.common.components.SettingsTabWithTimePicker
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.ui.theme.FontSize


/**
 * Экран настроек.
 *
 * @param viewModel Модель представления для экрана настроек.
 */
@Composable
internal fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextForThisTheme(
            text = "Settings",
            fontSize = FontSize.big22
        )
        state.let {
            if (it.exerciseDuration != null && it.tempExerciseDuration != null) {
                SettingsTabWithTimePicker(
                    time = it.exerciseDuration,
                    timeToChangeInDialog = it.tempExerciseDuration,
                    isTimePickerShowed = it.isExerciseDurationDialogShowed,
                    description = stringResource(R.string.time_of_exercise),
                    switchTimePickerIsShowed = {
                        viewModel.handleIntent(SettingsIntent.SwitchExerciseDurationDialogIsShowed)
                    },
                    updateTime = { newTime ->
                        viewModel.handleIntent(SettingsIntent.UpdateTempExerciseDuration(newTime))
                    },
                    applyTime = { viewModel.handleIntent(SettingsIntent.ApplyExerciseDuration) },
                    resetTime = { viewModel.handleIntent(SettingsIntent.ResetExerciseDuration) }
                )
            }

            if (it.breakDuration != null && it.tempBreakDuration != null) {
                SettingsTabWithTimePicker(
                    time = it.breakDuration,
                    timeToChangeInDialog = it.tempBreakDuration,
                    isTimePickerShowed = it.isBreakDurationDialogShowed,
                    description = stringResource(R.string.time_of_break),
                    switchTimePickerIsShowed = {
                        viewModel.handleIntent(SettingsIntent.SwitchBreakDurationDialogIsShowed)
                    },
                    updateTime = { newTime ->
                        viewModel.handleIntent(SettingsIntent.UpdateTempBreakDuration(newTime))
                    },
                    applyTime = { viewModel.handleIntent(SettingsIntent.ApplyBreakDuration) },
                    resetTime = { viewModel.handleIntent(SettingsIntent.ResetBreakDuration) }
                )
            }

            it.exercises.forEach { exercise ->
                exercise.ExerciseString {
                    viewModel.handleIntent(SettingsIntent.UpdateTraining(it))
                }
            }
            TextForThisTheme(
                text = "${state.appVersion.second}(${state.appVersion.first})",
                fontSize = FontSize.small17
            )
        }
    }
}


