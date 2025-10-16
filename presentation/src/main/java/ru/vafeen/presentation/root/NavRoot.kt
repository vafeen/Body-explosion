package ru.vafeen.presentation.root


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.common.components.UpdateAvailable
import ru.vafeen.presentation.common.components.UpdateProgress
import ru.vafeen.presentation.common.utils.getAppVersion
import ru.vafeen.presentation.features.training.TrainingScreen
import ru.vafeen.presentation.ui.theme.AppTheme


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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppTheme.colors.background
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.weight(1f)) {
                TrainingScreen()
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("${version.second}(${version.first})")
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
