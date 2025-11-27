package ru.vafeen.presentation.features.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vafeen.presentation.navigation.Screen
import ru.vafeen.presentation.root.NavRootIntent

@HiltViewModel(assistedFactory = WorkoutViewModel.Factory::class)
internal class WorkoutViewModel @AssistedInject constructor(
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit
) : ViewModel() {

    fun handleIntent(intent: WorkoutIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                WorkoutIntent.NavigateToHistory -> navigateToHistory()
                WorkoutIntent.NavigateToSettings -> navigateToSettings()
            }
        }
    }

    private fun navigateToHistory() = sendRootIntent(NavRootIntent.NavigateTo(Screen.History))
    private fun navigateToSettings() =
        sendRootIntent(NavRootIntent.NavigateTo(Screen.Settings))

    @AssistedFactory
    interface Factory {
        /**
         * Создает экземпляр [WorkoutViewModel].
         * @param sendRootIntent Функция для отправки навигационных интентов.
         */
        fun create(sendRootIntent: (NavRootIntent) -> Unit): WorkoutViewModel
    }
}