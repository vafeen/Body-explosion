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
import java.time.LocalTime
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
     *
     * @param intent Намерение пользователя.
     */
    fun handleIntent(intent: SettingsIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is SettingsIntent.UpdateSettings -> updateSettings(intent.updating)

                is SettingsIntent.UpdateTraining -> updateTraining(intent.training)
                SettingsIntent.SwitchBreakDurationDialogIsShowed -> switchBreakDurationDialogIsShowed()
                SettingsIntent.SwitchExerciseDurationDialogIsShowed -> switchExerciseDurationDialogIsShowed()

                is SettingsIntent.UpdateTempExerciseDuration -> updateTempExerciseDuration(intent.duration)
                SettingsIntent.ApplyExerciseDuration -> applyExerciseDuration()
                SettingsIntent.ResetExerciseDuration -> resetExerciseDuration()

                is SettingsIntent.UpdateTempBreakDuration -> updateTempBreakDuration(intent.duration)
                SettingsIntent.ApplyBreakDuration -> applyBreakDuration()
                SettingsIntent.ResetBreakDuration -> resetBreakDuration()
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsManager.settingsFlow.collect {
                _state.update { state ->
                    state.copy(
                        exerciseDuration = it.exerciseDuration,
                        breakDuration = it.breakDuration,
                        tempExerciseDuration = it.exerciseDuration,
                        tempBreakDuration = it.breakDuration
                    )
                }
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            trainingLocalRepository.getAllTrainings().collect {
                _state.update { state -> state.copy(trainings = it) }
            }
        }
    }

    private fun switchBreakDurationDialogIsShowed() = _state.update {
        it.copy(
            isBreakDurationDialogShowed = !it.isBreakDurationDialogShowed,
            tempBreakDuration = if (it.isBreakDurationDialogShowed) it.breakDuration else it.tempBreakDuration
        )
    }

    private fun switchExerciseDurationDialogIsShowed() = _state.update {
        it.copy(
            isExerciseDurationDialogShowed = !it.isExerciseDurationDialogShowed,
            tempExerciseDuration =
                if (it.isExerciseDurationDialogShowed) it.exerciseDuration else it.tempExerciseDuration
        )
    }

    /**
     * Обновляет тренировку в локальной базе данных.
     *
     * @param training Тренировка для обновления.
     */
    private suspend fun updateTraining(training: Training) =
        trainingLocalRepository.insert(listOf(training))

    /**
     * Обновляет временную длительность упражнения в состоянии.
     *
     * @param duration Новая временная длительность упражнения.
     */
    private fun updateTempExerciseDuration(duration: LocalTime) =
        _state.update { it.copy(tempExerciseDuration = duration) }

    /**
     * Применяет временную длительность упражнения к реальному значению и сохраняет настройки.
     * Обновляет состояние с новым значением.
     */
    private fun applyExerciseDuration() {
        val newDuration = state.value.tempExerciseDuration ?: return
        settingsManager.save {
            it.copy(exerciseDuration = newDuration)
        }
        _state.update {
            it.copy(
                exerciseDuration = newDuration,
                isExerciseDurationDialogShowed = false
            )
        }
    }

    /**
     * Сбрасывает длительность упражнения к значению по умолчанию и обновляет состояние и настройки.
     */
    private fun resetExerciseDuration() {
        var defaultTime: LocalTime? = null
        settingsManager.save {
            it.copy(exerciseDuration = it.defaultExerciseDuration.also { time ->
                defaultTime = time
            })
        }
        _state.update {
            it.copy(
                exerciseDuration = defaultTime,
                tempExerciseDuration = defaultTime,
                isExerciseDurationDialogShowed = false
            )
        }
    }

    /**
     * Обновляет временную длительность перерыва в состоянии.
     *
     * @param duration Новая временная длительность перерыва.
     */
    private fun updateTempBreakDuration(duration: LocalTime) =
        _state.update { it.copy(tempBreakDuration = duration) }

    /**
     * Применяет временную длительность перерыва к реальному значению и сохраняет настройки.
     * Обновляет состояние с новым значением.
     */
    private fun applyBreakDuration() {
        val newDuration = state.value.tempBreakDuration ?: return
        settingsManager.save {
            it.copy(breakDuration = newDuration)
        }
        _state.update {
            it.copy(
                breakDuration = newDuration,
                isBreakDurationDialogShowed = false
            )
        }
    }

    /**
     * Сбрасывает длительность перерыва к значению по умолчанию и обновляет состояние и настройки.
     */
    private fun resetBreakDuration() {
        var defaultTime: LocalTime? = null
        settingsManager.save {
            it.copy(breakDuration = it.defaultBreakDuration.also { time ->
                defaultTime = time
            })
        }
        _state.update {
            it.copy(
                breakDuration = defaultTime,
                tempBreakDuration = defaultTime,
                isBreakDurationDialogShowed = false
            )
        }
    }

    /**
     * Обновляет настройки приложения.
     *
     * @param updating Лямбда-выражение, которое принимает текущие настройки и возвращает обновленные.
     */
    private fun updateSettings(updating: (Settings) -> Settings) =
        settingsManager.save(updating)
}
