package ru.vafeen.presentation.features.training

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.components.ExerciseString2
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.features.workout.target
import ru.vafeen.presentation.root.NavRootIntent
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.Colors
import ru.vafeen.presentation.ui.theme.FontSize

/**
 * Главный экран тренировки, который управляет отображением различных состояний тренировки.
 *
 * @param sendRootIntent Функция для отправки намерений в корневой навигационный граф.
 */
@Composable
internal fun TrainingScreen(
    sendRootIntent: (NavRootIntent) -> Unit,
) {
    val viewModel = hiltViewModel<TrainingViewModel, TrainingViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(sendRootIntent)
        }
    )
    val state by viewModel.state.collectAsState()
    TrainingScreenByState(state, viewModel)
}

@Composable
internal fun TrainingScreenByState(
    state: TrainingState,
    viewModel: TrainingViewModel,
) {
    val context = LocalContext.current
    LaunchedEffect(null) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is TrainingEffect.ShowToast -> Toast.makeText(
                    context,
                    effect.message,
                    effect.length
                ).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                when (state) {
                    is TrainingState.InProgress -> Colors.exerciseColor
                    is TrainingState.Break, is TrainingState.PausedBreak, is TrainingState.PausedTraining -> Colors.breakColor
                    else -> AppTheme.colors.background
                }
            )
    ) {

        when (state) {
            is TrainingState.NotStarted -> NotStartedPane(
                state = state,
                modifier = Modifier.weight(1f),
                sendIntent = viewModel::handleIntent
            )

            is TrainingState.InProgress -> TrainingPane(
                modifier = Modifier.weight(1f),
                state = state,
                sendIntent = viewModel::handleIntent
            )

            is TrainingState.Break -> BreakPane(
                modifier = Modifier.weight(1f),
                state = state,
                sendIntent = viewModel::handleIntent
            )

            is TrainingState.PausedBreak -> PausedBreakPane(
                modifier = Modifier.weight(1f),
                state = state,
                sendIntent = viewModel::handleIntent
            )

            is TrainingState.PausedTraining -> PausedTrainingPane(
                modifier = Modifier.weight(1f),
                state = state,
                sendIntent = viewModel::handleIntent
            )
        }
    }
}

/**
 * Панель, отображающаяся во время выполнения упражнения.
 *
 * @param state Текущее состояние тренировки [TrainingState.InProgress].
 * @param sendIntent Функция для отправки намерений в ViewModel.
 */
@Composable
internal fun TrainingPane(
    modifier: Modifier,
    state: TrainingState.InProgress,
    sendIntent: (TrainingIntent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TextForThisTheme(
            text = stringResource(id = R.string.current_exercise),
            fontSize = FontSize.medium19,
        )
        TextForThisTheme(
            text = "[${state.currentExercise + 1}/${state.exercises.size}] ${state.exercises[state.currentExercise].name}",
            fontSize = FontSize.big22,
            fontWeight = FontWeight.Bold
        )
        Timer(
            modifier = Modifier.size(200.dp),
            currentSecondsLeft = state.secondsLeft,
            totalSeconds = state.secondsOnOneExercise,
            backgroundArcColor = AppTheme.colors.defaultButtonColor,
            activeArcColor = AppTheme.colors.mainColor
        )
        PauseAndStopExercise(sendIntent = sendIntent)
    }
}

/**
 * Компонент с кнопками "Пауза" и "Стоп".
 *
 * @param modifier Модификатор для настройки внешнего вида.
 * @param sendIntent Функция для отправки намерений в ViewModel.
 */
@Composable
internal fun PauseAndStopExercise(
    modifier: Modifier = Modifier,
    sendIntent: (TrainingIntent) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { sendIntent(TrainingIntent.PauseTraining) }) {
            Text(text = stringResource(R.string.pause))
        }
        TextForThisTheme(
            text = stringResource(R.string.finish), modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .border(
                    border = BorderStroke(1.dp, AppTheme.colors.text),
                    RoundedCornerShape(10.dp)
                )
                .combinedClickable(
                    onLongClick = { sendIntent(TrainingIntent.StopTraining) },
                    onClick = {
                        sendIntent(
                            TrainingIntent.ShowToast(
                                message = context.getString(R.string.long_tap_please),
                                duration = Toast.LENGTH_SHORT
                            )
                        )
                    }
                )
                .padding(10.dp),
            fontSize = FontSize.medium19)

    }
}

/**
 * Панель, отображающаяся, когда тренировка еще не началась.
 *
 * @param sendIntent Функция для отправки намерений в ViewModel.
 */
@Composable
internal fun NotStartedPane(
    state: TrainingState.NotStarted,
    modifier: Modifier,
    sendIntent: (TrainingIntent) -> Unit
) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextForThisTheme(
            modifier = Modifier.padding(10.dp),
            text = target,
            fontSize = FontSize.big22
        )
        LazyColumn {
            items(items = state.exercises) {
                it.ExerciseString2 { newExercise ->
                    sendIntent(TrainingIntent.UpdateExercise(newExercise))
                }
            }
        }
        StartButton { sendIntent(TrainingIntent.StartTraining) }
    }
}

/**
 * Панель, отображающаяся во время перерыва между упражнениями.
 *
 * @param state Текущее состояние перерыва [TrainingState.Break].
 * @param sendIntent Функция для отправки намерений в ViewModel.
 */
@Composable
internal fun BreakPane(
    modifier: Modifier,
    state: TrainingState.Break, sendIntent: (TrainingIntent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TextForThisTheme(
            text = stringResource(id = R.string.rest),
            fontSize = FontSize.medium19,
        )
        TextForThisTheme(
            text = "${stringResource(id = R.string.exercises_done)} ${state.nextExercise}/${state.exercises.size}",
            fontSize = FontSize.medium19,
        )
        TextForThisTheme(
            text = stringResource(id = R.string.next_exercise),
            fontSize = FontSize.medium19,
        )
        TextForThisTheme(
            text = "[${state.nextExercise + 1}/${state.exercises.size}]${state.exercises[state.nextExercise].name}",
            fontSize = FontSize.big22,
            fontWeight = FontWeight.Bold
        )

        Timer(
            modifier = Modifier.size(200.dp),
            currentSecondsLeft = state.secondsLeft,
            totalSeconds = state.secondsForBreak,
            backgroundArcColor = AppTheme.colors.defaultButtonColor,
            activeArcColor = AppTheme.colors.mainColor
        )
        PauseAndStopExercise(sendIntent = sendIntent)
    }
}

/**
 * Панель, отображающаяся, когда перерыв приостановлен.
 *
 * @param state Текущее состояние приостановленного перерыва [TrainingState.PausedBreak].
 * @param sendIntent Функция для отправки намерений в ViewModel.
 */
@Composable
internal fun PausedBreakPane(
    modifier: Modifier,
    state: TrainingState.PausedBreak,
    sendIntent: (TrainingIntent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextForThisTheme(
            text = stringResource(id = R.string.rest_pause),
            fontSize = FontSize.medium19
        )
        TextForThisTheme(
            text = stringResource(
                id = R.string.exercises_done,
                state.nextExercise,
                state.exercises.size
            ),
            fontSize = FontSize.medium19,
        )
        TextForThisTheme(
            text = stringResource(id = R.string.next_exercise),
            fontSize = FontSize.medium19,
        )
        TextForThisTheme(
            text = "[${state.nextExercise + 1}/${state.exercises.size}] ${state.exercises[state.nextExercise].name}",
            fontSize = FontSize.big22,
            fontWeight = FontWeight.Bold
        )
        Button(onClick = { sendIntent(TrainingIntent.StartTraining) }) {
            Text(text = stringResource(id = R.string.continue_button))
        }
    }
}

/**
 * Панель, отображающаяся, когда тренировка приостановлена.
 *
 * @param state Текущее состояние приостановленной тренировки [TrainingState.PausedTraining].
 * @param sendIntent Функция для отправки намерений в ViewModel.
 */
@Composable
internal fun PausedTrainingPane(
    modifier: Modifier,
    state: TrainingState.PausedTraining,
    sendIntent: (TrainingIntent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextForThisTheme(
            text = stringResource(id = R.string.training_pause),
            fontSize = FontSize.medium19
        )
        TextForThisTheme(
            text = stringResource(id = R.string.current_exercise),
            fontSize = FontSize.medium19,
        )
        TextForThisTheme(
            text = "[${state.currentExercise + 1}/${state.exercises.size}]${state.exercises[state.currentExercise].name}",
            fontSize = FontSize.big22,
            fontWeight = FontWeight.Bold
        )
        Button(onClick = { sendIntent(TrainingIntent.StartTraining) }) {
            Text(text = stringResource(id = R.string.continue_button))
        }
    }
}

/**
 * Кнопка для начала тренировки.
 *
 * @param onClick Функция, вызываемая при нажатии на кнопку.
 */
@Composable
private fun StartButton(onClick: () -> Unit) {
    Button(onClick) {
        Text(text = stringResource(id = R.string.start_training))
    }
}

/**
 * Компонент таймера, отображающий оставшееся время в виде кругового индикатора.
 *
 * @param modifier Модификатор для настройки внешнего вида.
 * @param currentSecondsLeft Оставшееся время в секундах.
 * @param totalSeconds Общее время в секундах.
 * @param backgroundArcColor Цвет фона кругового индикатора.
 * @param activeArcColor Цвет активной части кругового индикатора.
 */
@Composable
private fun Timer(
    modifier: Modifier = Modifier,
    currentSecondsLeft: Int,
    totalSeconds: Int,
    backgroundArcColor: Color,
    activeArcColor: Color,
) {
    val progress = currentSecondsLeft / totalSeconds.toFloat()
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokeWidth = 12f
            val radius = size.minDimension / 2 - strokeWidth / 2

            withTransform({
                rotate(-90f, center) // Начинаем отрисовку сверху
            }) {
                // Фон (серая часть) - показывает прошедшее время
                drawArc(
                    color = backgroundArcColor,
                    startAngle = 0f,
                    sweepAngle = 360f * (1f - progress),
                    useCenter = false,
                    topLeft = center - Offset(radius, radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth)
                )

                // Активная часть (цветная/красная) - показывает оставшееся время
                drawArc(
                    color = activeArcColor,
                    startAngle = 360f * (1f - progress),
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    topLeft = center - Offset(radius, radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth)
                )
            }
        }
        TextForThisTheme(
            text = "$currentSecondsLeft",
            fontSize = FontSize.gigant,
        )
    }
}
