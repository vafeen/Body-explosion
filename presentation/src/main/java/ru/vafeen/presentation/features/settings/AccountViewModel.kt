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
import ru.vafeen.domain.models.Settings
import ru.vafeen.domain.repository.ExerciseRepository
import ru.vafeen.presentation.common.utils.getAppVersion
import javax.inject.Inject

/**
 * ViewModel для экрана настроек.
 *
 * @property exerciseRepository Репозиторий для работы с тренировками в локальной базе данных.
 * @property settingsManager Менеджер для работы с настройками приложения.
 * @param context Контекст приложения.
 */
@HiltViewModel
internal class AccountViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val settingsManager: SettingsManager,
    @ApplicationContext context: Context
) : ViewModel() {
    private val application = context as Application

    private val _state = MutableStateFlow(
        AccountState(appVersion = application.getAppVersion())
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
    fun handleIntent(intent: AccountIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is AccountIntent.UpdateAccount -> updateSettings(intent.updating)
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsManager.settingsFlow.collect {
                _state.update { state ->
                    state.copy(
                        user = it.user
                    )
                }
            }
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
