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
import ru.vafeen.presentation.common.utils.getAppVersion
import ru.vafeen.presentation.navigation.Screen
import javax.inject.Inject

@HiltViewModel
internal class NavRootViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val releaseRepository: ReleaseRepository,
    private val refresher: Refresher,
) : ViewModel() {
    private val application: Application = context as Application
    private val _state = MutableStateFlow(NavRootState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<NavRootEffect>()
    val effects = _effects.asSharedFlow()
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

    private suspend fun navigateTo(screen: Screen) =
        _effects.emit(NavRootEffect.NavigateTo(screen = screen))

    private suspend fun navigateBack() = _effects.emit(NavRootEffect.NavigateBack)

    private suspend fun checkUpdates() {
        val result = releaseRepository.getLatestRelease()
        val (_, appVersionName) = application.getAppVersion()
        if (result is ResponseResult.Success<Release>) {
            val release = result.data
            _state.update {
                it.copy(release = release, isUpdateNeeded = appVersionName != release.tagName)
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            refresher.progressFlow.collect { status ->
                _state.update {
                    it.copy(
                        isUpdateInProcess = status is DownloadStatus.Started || status is DownloadStatus.InProgress,
                        percentage = if (status is DownloadStatus.InProgress) status.percentage / 100f else 0f
                    )
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