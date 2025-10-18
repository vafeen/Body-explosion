package ru.vafeen.presentation.features.settings

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vafeen.domain.datastore.SettingsManager
import ru.vafeen.domain.local_database.TrainingLocalRepository
import ru.vafeen.domain.models.Settings
import ru.vafeen.domain.models.Training
import ru.vafeen.presentation.common.utils.getAppVersion
import javax.inject.Inject

/**
 * ViewModel для экрана настроек.
 *
 * @property trainingLocalRepository Репозиторий для работы с тренировками в локальной базе данных.
 * @property settingsManager Менеджер для работы с настройками приложения.
 * @param context Контекст приложения.
 */
@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val trainingLocalRepository: TrainingLocalRepository,
    private val settingsManager: SettingsManager,
    @ApplicationContext context: Context
) : ViewModel() {
    private val application = context as Application

    private val _state = MutableStateFlow(
        SettingsState(appVersion = application.getAppVersion())
    )

    /**
     * Состояние экрана настроек.
     */
    val state = _state.asStateFlow()

    /**
     * Обрабатывает намерения пользователя.
     * @param intent Намерение пользователя.
     */
    fun handleIntent(intent: SettingsIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is SettingsIntent.UpdateSettings -> updateSettings(intent.updating)
                SettingsIntent.ResetDuration -> resetDuration()
                is SettingsIntent.UpdateTraining -> updateTraining(intent.training)
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsManager.settingsFlow.collect {
                _state.update { state ->
                    state.copy(
                        exerciseDurationSeconds = it.exerciseDurationSeconds,
                        breakDurationSeconds = it.breakDurationSeconds,
                    )
                }
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            trainingLocalRepository.getAllTrainings().collect {
                _state.update { state ->
                    state.copy(trainings = it)
                }
            }
        }
    }

    /**
     * Обновляет тренировку в локальной базе данных.
     * @param training Тренировка для обновления.
     */
    private suspend fun updateTraining(training: Training) =
        trainingLocalRepository.insert(listOf(training))

    /**
     * Сбрасывает длительность упражнения и перерыва к значениям по умолчанию.
     */
    private fun resetDuration() = settingsManager.save {
        it.copy(
            exerciseDurationSeconds = it.defaultExerciseDurationSeconds,
            breakDurationSeconds = it.defaultBreakDurationSeconds
        )
    }

    /**
     * Обновляет настройки приложения.
     * @param updating Лямбда-выражение, которое принимает текущие настройки и возвращает обновленные.
     */
    private fun updateSettings(updating: (Settings) -> Settings) = settingsManager.save(updating)


}