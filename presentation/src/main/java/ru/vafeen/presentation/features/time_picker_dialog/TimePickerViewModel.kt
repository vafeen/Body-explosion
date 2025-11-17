package ru.vafeen.presentation.features.time_picker_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime

/**
 * ViewModel для управления состоянием и действиями диалога выбора времени.
 *
 * Работает по архитектурному паттерну MVI: получает интенты от UI,
 * обновляет состояние и вызывает колбэки при подтверждении или закрытии диалога.
 *
 * @property initialTime Начальное время, отображаемое в диалоге.
 * @property onTimeSelected Функция, вызываемая при подтверждении выбора времени.
 * @property onDismissRequest Функция, вызываемая при закрытии диалога.
 */
@HiltViewModel(assistedFactory = TimePickerViewModel.Factory::class)
internal class TimePickerViewModel @AssistedInject constructor(
    @Assisted private val initialTime: LocalTime,
    @Assisted private val onTimeSelected: (LocalTime) -> Unit,
    @Assisted private val onDismissRequest: () -> Unit,
) : ViewModel() {

    /** Внутреннее состояние диалога выбора времени. */
    private val _state = MutableStateFlow(TimePickerDialogState(initialTime))

    /** Публичный поток состояния для подписки из Composable. */
    val state = _state.asStateFlow()

    /**
     * Обрабатывает пользовательские действия (интенты) и выполняет соответствующую логику.
     *
     * @param intent Интент, описывающий действие пользователя.
     */
    fun handleIntent(intent: TimePickerIntent) {
        viewModelScope.launch(Dispatchers.Main) {
            when (intent) {
                is TimePickerIntent.UpdateTime -> updateTime(intent.time)
                TimePickerIntent.Confirm -> confirm()
                TimePickerIntent.Cancel -> cancel()
            }
        }
    }

    /**
     * Обновляет текущее выбранное время в состоянии.
     *
     * @param time Новое время, выбранное пользователем.
     */
    private fun updateTime(time: LocalTime) {
        _state.update { it.copy(currentTime = time) }
    }

    /**
     * Подтверждает выбор времени, вызывает колбэк [onTimeSelected]
     * и закрывает диалог через [onDismissRequest].
     */
    private fun confirm() {
        onTimeSelected(_state.value.currentTime)
        onDismissRequest()
    }

    /**
     * Отменяет выбор времени и закрывает диалог без сохранения изменений.
     */
    private fun cancel() = onDismissRequest()

    /**
     * Фабрика для создания экземпляров [TimePickerViewModel] через Assisted Injection.
     */
    @AssistedFactory
    interface Factory {

        /**
         * Создает экземпляр [TimePickerViewModel].
         *
         * @param initialTime Начальное время, отображаемое в диалоге.
         * @param onTimeSelected Колбэк, вызываемый при подтверждении времени.
         * @param onDismissRequest Колбэк, вызываемый при закрытии диалога.
         * @return Новый экземпляр [TimePickerViewModel].
         */
        fun create(
            @Assisted initialTime: LocalTime,
            @Assisted onTimeSelected: (LocalTime) -> Unit,
            @Assisted onDismissRequest: () -> Unit
        ): TimePickerViewModel
    }
}
