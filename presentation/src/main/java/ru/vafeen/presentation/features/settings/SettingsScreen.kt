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
            if (it.exerciseDurationMillis != null && it.breakDurationMillis != null) {
                TextForThisTheme(
                    modifier = Modifier.clickable {
                        viewModel.handleIntent(SettingsIntent.UpdateSettings { settings ->
                            settings.copy(exerciseDurationMillis = settings.exerciseDurationMillis + 1)
                        })
                    },
                    text = "exerciseDurationMillis=${it.exerciseDurationMillis}",
                    fontSize = FontSize.medium19
                )
                TextForThisTheme(
                    modifier = Modifier.clickable {
                        viewModel.handleIntent(SettingsIntent.UpdateSettings { settings ->
                            settings.copy(breakDurationMillis = settings.breakDurationMillis + 1)
                        })
                    },
                    text = "breakDurationMillis=${it.breakDurationMillis}",
                    fontSize = FontSize.medium19
                )
            }
        }
    }
}