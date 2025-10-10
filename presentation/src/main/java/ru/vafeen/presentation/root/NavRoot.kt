package ru.vafeen.presentation.root


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.features.training.TrainingScreen
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Корневой Composable, который настраивает Scaffold
 *
 * @param viewModel ViewModel для NavRoot.
 */
@Composable
internal fun NavRoot(viewModel: NavRootViewModel = hiltViewModel()) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppTheme.colors.background
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TrainingScreen()
        }
    }
}
