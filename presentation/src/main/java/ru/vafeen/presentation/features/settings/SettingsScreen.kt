package ru.vafeen.presentation.features.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.ui.theme.FontSize


@Composable
internal fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextForThisTheme(
            text = "Settings",
            fontSize = FontSize.big22
        )
        state.let {
            if (it.exerciseDurationSeconds != null && it.breakDurationSeconds != null) {
                TextForThisTheme(
                    modifier = Modifier.clickable {
                        viewModel.handleIntent(SettingsIntent.UpdateSettings { settings ->
                            settings.copy(exerciseDurationSeconds = settings.exerciseDurationSeconds + 1)
                        })
                    },
                    text = "exerciseDurationSeconds=${it.exerciseDurationSeconds}",
                    fontSize = FontSize.medium19
                )
                TextForThisTheme(
                    modifier = Modifier.clickable {
                        viewModel.handleIntent(SettingsIntent.UpdateSettings { settings ->
                            settings.copy(breakDurationSeconds = settings.breakDurationSeconds + 1)
                        })
                    },
                    text = "breakDurationSeconds=${it.breakDurationSeconds}",
                    fontSize = FontSize.medium19
                )
                TextForThisTheme(
                    modifier = Modifier.clickable {
                        viewModel.handleIntent(SettingsIntent.UpdateSettings { settings ->
                            settings.copy(
                                breakDurationSeconds = 10,
                                exerciseDurationSeconds = 60
                            )
                        })
                    },
                    text = "reset",
                    fontSize = FontSize.medium19
                )
            }
        }
    }
}