package ru.vafeen.presentation.features.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.ui.theme.FontSize


/**
 * Экран настроек.
 *
 * @param viewModel Модель представления для экрана настроек.
 */
@Composable
internal fun AccountScreen(viewModel: AccountViewModel = hiltViewModel()) {
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
            text = stringResource(R.string.account),
            fontSize = FontSize.big22
        )
        state.let {
            state.user?.let { (name, username) ->
                Card {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextForThisTheme(
                            text = stringResource(R.string.name),
                            fontSize = FontSize.medium19
                        )
                        TextForThisTheme(text = name, fontSize = FontSize.medium19)
                    }
                }
                Card {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextForThisTheme(
                            text = stringResource(R.string.username),
                            fontSize = FontSize.medium19
                        )
                        TextForThisTheme(text = username, fontSize = FontSize.medium19)
                    }
                }
            }

//            it.exercises.forEach { exercise ->
//                exercise.ExerciseString {
//                    viewModel.handleIntent(SettingsIntent.UpdateTraining(it))
//                }
//            }
            TextForThisTheme(
                text = "${state.appVersion.second}(${state.appVersion.first})",
                fontSize = FontSize.small17
            )
        }
    }
}


