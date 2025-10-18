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
import ru.vafeen.presentation.common.utils.getAppVersion
import javax.inject.Inject

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
    val state = _state.asStateFlow()
    fun handleIntent(intent: SettingsIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is SettingsIntent.UpdateSettings -> updateSettings(intent.updating)
                SettingsIntent.ResetDuration -> resetDuration()
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

    private fun resetDuration() = settingsManager.save {
        it.copy(
            exerciseDurationSeconds = it.defaultExerciseDurationSeconds,
            breakDurationSeconds = it.defaultBreakDurationSeconds
        )
    }

    private fun updateSettings(updating: (Settings) -> Settings) = settingsManager.save(updating)


}