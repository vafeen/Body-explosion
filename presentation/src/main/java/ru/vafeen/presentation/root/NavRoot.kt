package ru.vafeen.presentation.root

import android.util.Log
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import ru.vafeen.presentation.common.components.UpdateAvailable
import ru.vafeen.presentation.common.components.UpdateProgress
import ru.vafeen.presentation.common.utils.copyTextToClipBoard
import ru.vafeen.presentation.features.history.HistoryScreen
import ru.vafeen.presentation.features.settings.SettingsScreen
import ru.vafeen.presentation.features.training.TrainingScreen
import ru.vafeen.presentation.features.user_sign.UserSignScreen
import ru.vafeen.presentation.features.workout.WorkoutScreen
import ru.vafeen.presentation.navigation.Screen
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Корневой Composable-компонент приложения, отвечающий за:
 * - отображение версии приложения,
 * - проверку и установку обновлений,
 * - навигацию между экранами (обучение, настройки),
 * - централизованное отображение ошибок через Snackbar.
 *
 * Использует unidirectional data flow: ViewModel генерирует эффекты навигации и ошибки,
 * которые обрабатываются в UI-слое.
 *
 * @param viewModel ViewModel корневого экрана, инжектируемый через Hilt.
 */
@Composable
internal fun NavRoot(viewModel: NavRootViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(NavRootIntent.CheckUpdates)
    }

    val backStack = rememberNavBackStack(Screen.UserSign)

    LaunchedEffect(Unit) {
        Log.e("tag", "effects runs")
        viewModel.effects.collect { effect ->
            Log.e("tag", "collect")
            when (effect) {
                is NavRootEffect.NavigateTo -> {
                    // если экран уже есть в стеке, не добавляем его повторно
                    if (backStack.lastOrNull() != effect.screen) {
                        backStack.add(effect.screen)
                    }
                }

                is NavRootEffect.ReplaceRoot -> {
                    backStack.clear()
                    backStack.add(effect.screen)
                    Log.e("tag", "root replaced on ${effect.screen}")
                }

                NavRootEffect.NavigateBack -> backStack.removeLastOrNull()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.messages.collect { message ->
            val result = snackbarHostState.showSnackbar(
                message = message.text,
                actionLabel = message.button
            )
            when (result) {
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> context.copyTextToClipBoard(
                    label = if (message.clipboardText != null) message.text else "",
                    text = message.clipboardText ?: message.text
                )
            }
        }
    }
    LaunchedEffect(null) {
        viewModel.handleIntent(NavRootIntent.CheckAuth)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppTheme.colors.background,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        val nullTransitionSpec = remember {
            ContentTransform(
                fadeIn(animationSpec = tween(0)),
                fadeOut(animationSpec = tween(0)),
            )
        }

        Column(modifier = Modifier.padding(innerPadding)) {
            NavDisplay(
                backStack = backStack,
                modifier = Modifier.weight(1f),
                onBack = { viewModel.handleIntent(NavRootIntent.NavigateBack) },
                entryProvider = entryProvider {
                    entry<Screen.UserSign> {
                        UserSignScreen(onAuthSuccess = {
                            viewModel.handleIntent(
                                NavRootIntent.ReplaceRoot(Screen.Training)
                            )
                        })
                    }
                    entry<Screen.Training> { TrainingScreen(viewModel::handleIntent) }
                    entry<Screen.Settings> { SettingsScreen() }
                    entry<Screen.History> { HistoryScreen() }
                    entry<Screen.Workout> { WorkoutScreen(viewModel::handleIntent) }
                },
                transitionSpec = { nullTransitionSpec },
                popTransitionSpec = { nullTransitionSpec },
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                state.let {
                    if (it.release != null && it.isUpdateNeeded) {
                        UpdateAvailable(it.release) {
                            viewModel.handleIntent(NavRootIntent.UpdateApp)
                        }
                    }
                }

                // Показывать индикатор загрузки, если обновление в процессе
                if (state.isUpdateInProcess) {
                    UpdateProgress(percentage = state.percentage)
                }
            }
        }
    }
}