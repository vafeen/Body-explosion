package ru.vafeen.presentation.root

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vafeen.domain.models.Release
import ru.vafeen.domain.network.result.DownloadStatus
import ru.vafeen.domain.network.result.ResponseResult
import ru.vafeen.domain.network.service.Refresher
import ru.vafeen.domain.network.service.ReleaseRepository
import ru.vafeen.domain.service.ErrorShower
import ru.vafeen.presentation.common.utils.getAppVersion
import ru.vafeen.presentation.navigation.Screen
import javax.inject.Inject

/**
 * ViewModel корневого экрана приложения, управляющий проверкой и установкой обновлений,
 * навигацией между экранами и отображением ошибок.
 *
 * @property releaseRepository Репозиторий для получения информации о последнем релизе.
 * @property refresher Сервис для загрузки и установки APK-обновлений.
 * @property errorShower Компонент для централизованного сбора и отображения ошибок.
 * @property application Кэшированный экземпляр Application, полученный из контекста.
 */
@HiltViewModel
internal class NavRootViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val releaseRepository: ReleaseRepository,
    private val refresher: Refresher,
    private val errorShower: ErrorShower
) : ViewModel() {

    /**
     * Специальное значение версии, используемое для обозначения локальной (не релизной) сборки.
     * Такие сборки не участвуют в проверке обновлений.
     */
    private val undefinedVersion = "undefinedVersionForDevelopment"

    /**
     * Кэшированный экземпляр Application, необходимый для получения метаданных приложения.
     */
    private val application: Application = context as Application

    /**
     * Внутреннее изменяемое состояние корневого экрана.
     */
    private val _state = MutableStateFlow(
        NavRootState(
            version = application.getAppVersion()
        )
    )

    /**
     * Публичный поток состояния корневого экрана, доступный только для чтения.
     *
     * @return StateFlow, предоставляющий актуальное состояние UI.
     */
    val state = _state.asStateFlow()

    /**
     * Внутренний буфер для эмиссии навигационных эффектов.
     */
    private val _effects = MutableSharedFlow<NavRootEffect>()

    /**
     * Поток навигационных эффектов, доступный для подписки из UI.
     *
     * @return SharedFlow, эмитирующий эффекты навигации (например, переход на экран).
     */
    val effects = _effects.asSharedFlow()

    /**
     * Поток ошибок, делегированный от централизованного обработчика.
     *
     * @return SharedFlow ошибок, готовый к отображению в UI.
     */
    val errors = errorShower.errors

    /**
     * Обрабатывает входящий интент пользователя или системы.
     * Запускает соответствующее действие в фоновом потоке (IO).
     *
     * @param intent Интент, описывающий желаемое действие (проверка обновлений, навигация и т.д.).
     */
    fun handleIntent(intent: NavRootIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                NavRootIntent.CheckUpdates -> checkUpdates()
                NavRootIntent.UpdateApp -> updateApp()
                is NavRootIntent.NavigateTo -> navigateTo(intent.screen)
                NavRootIntent.NavigateBack -> navigateBack()
            }
        }
    }

    /**
     * Отправляет эффект навигации "перейти к указанному экрану".
     *
     * @param screen Целевой экран, на который необходимо перейти.
     */
    private suspend fun navigateTo(screen: Screen) =
        _effects.emit(NavRootEffect.NavigateTo(screen = screen))

    /**
     * Отправляет эффект навигации "назад".
     * Обычно приводит к возврату на предыдущий экран в стеке.
     */
    private suspend fun navigateBack() = _effects.emit(NavRootEffect.NavigateBack)

    /**
     * Проверяет наличие новой версии приложения на удалённом сервере.
     * Сравнивает тег последнего релиза с текущей версией приложения.
     * Если версии различаются и сборка не локальная — устанавливает флаг необходимости обновления.
     * В случае сетевой ошибки передаёт исключение в обработчик ошибок.
     */
    private suspend fun checkUpdates() {
        val result = releaseRepository.getLatestRelease()
        val (_, appVersionName) = application.getAppVersion()
        if (result is ResponseResult.Success<Release>) {
            val release = result.data
            _state.update {
                it.copy(
                    release = release,
                    isUpdateNeeded = appVersionName != release.tagName && appVersionName != undefinedVersion
                )
            }
        } else if (result is ResponseResult.Error<*>) {
            errorShower.showError(result.exception)
        }
    }


    /**
     * Инициализирующий блок, подписывающийся на прогресс загрузки обновления.
     * Обновляет состояние с учётом текущего прогресса или ошибки загрузки.
     * Запускается один раз при создании ViewModel.
     */
    init {
        viewModelScope.launch(Dispatchers.IO) {
            refresher.progressFlow.collect { status ->
                _state.update {
                    it.copy(
                        isUpdateInProcess = status is DownloadStatus.Started || status is DownloadStatus.InProgress,
                        percentage = if (status is DownloadStatus.InProgress) status.percentage / 100f else 0f
                    )
                }
                if (status is DownloadStatus.Error) {
                    errorShower.showError(status.exception)
                }
            }
        }
    }

    private suspend fun updateApp() {
        _state.value.release?.let { release ->
            _state.update { it.copy(isUpdateNeeded = false, isUpdateInProcess = true) }
            refresher.refresh(release.apkUrl, Refresher.APK_FILE_NAME)
        }
    }
}