package ru.vafeen.presentation.features.training

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.vafeen.presentation.root.NavRootIntent
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Главный экран тренировки, который управляет отображением различных состояний тренировки.
 *
 * @param viewModel ViewModel для экрана тренировки.
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
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { viewModel.handleIntent(TrainingIntent.NavigateToSettings) }) {
                Text("navigateToSettings")
            }
        }
        when (val currentState = state) {
            is TrainingState.NotStarted -> NotStartedPane(
                modifier = Modifier.weight(1f),
                sendIntent = viewModel::handleIntent
            )

            is TrainingState.InProgress -> TrainingPane(
                modifier = Modifier.weight(1f),
                state = currentState,
                sendIntent = viewModel::handleIntent
            )

            is TrainingState.Break -> BreakPane(
                modifier = Modifier.weight(1f),
                state = currentState,
                sendIntent = viewModel::handleIntent
            )

            is TrainingState.PausedBreak -> PausedBreakPane(
                modifier = Modifier.weight(1f),
                state = currentState,
                sendIntent = viewModel::handleIntent
            )

            is TrainingState.PausedTraining -> PausedTrainingPane(
                modifier = Modifier.weight(1f),
                state = currentState,
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
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Упражнение ${state.currentExercise + 1}/${state.totalExercises}",
            color = AppTheme.colors.text
        )
        Timer(
            modifier = Modifier.size(100.dp),
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
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { sendIntent(TrainingIntent.PauseTraining) }) {
            Text(text = "Пауза")
        }
        Text(
            text = "Стоп", modifier = Modifier
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
                                message = "long tap please",
                                duration = Toast.LENGTH_SHORT
                            )
                        )
                    }
                )
                .padding(10.dp))

    }
}

/**
 * Панель, отображающаяся, когда тренировка еще не началась.
 *
 * @param sendIntent Функция для отправки намерений в ViewModel.
 */
@Composable
internal fun NotStartedPane(
    modifier: Modifier,
    sendIntent: (TrainingIntent) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
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
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Отдых",
            color = AppTheme.colors.text
        )
        Text(
            text = "Упражнений сделано ${state.currentExercise + 1}/${state.totalExercises}",
            color = AppTheme.colors.text
        )
        Timer(
            modifier = Modifier.size(100.dp),
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
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Пауза", color = AppTheme.colors.text)
        Text(
            text = "Упражнений сделано ${state.currentExercise + 1}/${state.totalExercises}",
            color = AppTheme.colors.text
        )
        Button(onClick = { sendIntent(TrainingIntent.StartTraining) }) {
            Text(text = "Продолжить")
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
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Пауза", color = AppTheme.colors.text)
        Text(
            text = "Текущее упражнение ${state.currentExercise + 1}/${state.totalExercises}",
            color = AppTheme.colors.text
        )
        Button(onClick = { sendIntent(TrainingIntent.StartTraining) }) {
            Text(text = "Продолжить")
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
        Text(text = "Начать тренировку")
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
        Text(
            text = "$currentSecondsLeft",
            color = AppTheme.colors.text,
            fontSize = 20.sp
        )
    }
}
