package ru.vafeen.presentation.root


import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.common.components.UpdateAvailable
import ru.vafeen.presentation.common.components.UpdateProgress
import ru.vafeen.presentation.common.utils.getAppVersion
import ru.vafeen.presentation.features.settings.SettingsScreen
import ru.vafeen.presentation.features.training.TrainingScreen
import ru.vafeen.presentation.navigation.Screen
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize


/**
 * Корневой Composable, который настраивает Scaffold
 *
 * @param viewModel ViewModel для NavRoot.
 */
@Composable
internal fun NavRoot(viewModel: NavRootViewModel = hiltViewModel()) {
    val version = LocalContext.current.getAppVersion()
    val state by viewModel.state.collectAsState()
    LaunchedEffect(null) {
        viewModel.handleIntent(NavRootIntent.CheckUpdates)
    }
    val backStack = rememberNavBackStack(Screen.Training)
    LaunchedEffect(null) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is NavRootEffect.NavigateTo -> backStack.add(effect.screen)
                NavRootEffect.NavigateBack -> backStack.removeLastOrNull()
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppTheme.colors.background
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
                    entry<Screen.Training> { TrainingScreen(viewModel::handleIntent) }
                    entry<Screen.Settings> { SettingsScreen() }
                },
                transitionSpec = { nullTransitionSpec },
                popTransitionSpec = { nullTransitionSpec },
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextForThisTheme(
                    text = "${version.second}(${version.first})",
                    fontSize = FontSize.small17
                )
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
