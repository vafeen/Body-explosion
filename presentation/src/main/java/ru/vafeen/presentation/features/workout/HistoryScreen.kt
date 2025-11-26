package ru.vafeen.presentation.features.workout

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.common.components.WorkoutString
import ru.vafeen.presentation.ui.theme.FontSize

/**
 * Экран, отображающий историю тренировок.
 *
 * @param viewModel ViewModel для экрана истории.
 */
@Composable
internal fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState(HistoryState())
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