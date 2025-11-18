package ru.vafeen.presentation.features.user_sign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vafeen.domain.datastore.SettingsManager
import ru.vafeen.domain.network.result.ResponseResult
import ru.vafeen.domain.network.service.UserDataRepository
import javax.inject.Inject

/**
 * ViewModel для экрана входа и регистрации.
 * Управляет состоянием формы, обрабатывает вводимые данные и выполняет запросы
 * к серверу для аутентификации или создания нового пользователя.
 *
 * @property userDataRepository Репозиторий для взаимодействия с API аутентификации.
 * @property settingsManager Менеджер настроек для сохранения данных сессии.
 */
@HiltViewModel
internal class UserSignViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val settingsManager: SettingsManager,
) : ViewModel() {

    /**
     * Внутреннее изменяемое состояние UI.
     */
    private val _state = MutableStateFlow(UserSignState())

    /**
     * Публичное, только для чтения, состояние UI, на которое подписывается Compose-экран.
     */
    val state = _state.asStateFlow()

    /**
     * Внутренний изменяемый поток для отправки одноразовых событий (эффектов) в UI.
     */
    private val _effect = MutableSharedFlow<UserSignEffect>()

    /**
     * Публичный, только для чтения, поток эффектов для UI.
     */
    val effect = _effect.asSharedFlow()

    /**
     * Обрабатывает интенты, поступающие от UI.
     *
     * @param intent Интент, описывающий действие пользователя.
     */
    fun handleIntent(intent: UserSignIntent) {
        when (intent) {
            UserSignIntent.ToggleMode ->
                _state.value = _state.value.copy(isSignUp = !_state.value.isSignUp)

            is UserSignIntent.OnNameChanged ->
                _state.value = _state.value.copy(name = intent.name)

            is UserSignIntent.OnUsernameChanged ->
                _state.value = _state.value.copy(username = intent.username)

            is UserSignIntent.OnPasswordChanged ->
                _state.value = _state.value.copy(password = intent.password)

            UserSignIntent.Submit ->
                submit()
        }
    }

    /**
     * Выполняет отправку данных на сервер для входа или регистрации.
     * Проводит базовую валидацию полей перед отправкой.
     * В случае успеха сохраняет токены и ID пользователя, после чего эмитит эффект [UserSignEffect.AuthSuccess].
     * В случае ошибки обновляет состояние, добавляя в него текст ошибки.
     */
    private fun submit() {
        val currentState = _state.value
        if (currentState.isLoading) return

        val username = currentState.username.trim()
        val password = currentState.password.trim()
        val name = currentState.name.trim()

        if (username.isEmpty() || password.isEmpty() || (currentState.isSignUp && name.isEmpty())) {
            _state.value = currentState.copy(error = "Заполните все поля")
            return
        }

        _state.value = currentState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val result = if (currentState.isSignUp) {
                userDataRepository.signUp(name, username, password)
            } else {
                userDataRepository.signIn(username, password)
            }

            _state.value = _state.value.copy(isLoading = false)

            when {
                result is ResponseResult.Success -> {
                    val signResult = result.data
                    settingsManager.save { settings ->
                        settings.copy(
                            id = signResult.id,
                            accessToken = signResult.accessToken,
                            refreshToken = signResult.refreshToken
                        )
                    }
                    _effect.emit(
                        UserSignEffect.AuthSuccess(
                            id = signResult.id,
                            accessToken = signResult.accessToken,
                            refreshToken = signResult.refreshToken
                        )
                    )
                }

                result is ResponseResult.Error -> {
                    val message = result.exception.message ?: "Неизвестная ошибка"
                    _state.value = _state.value.copy(error = message)
                }
            }
        }
    }
}
