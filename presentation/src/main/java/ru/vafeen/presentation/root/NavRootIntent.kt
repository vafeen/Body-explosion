package ru.vafeen.presentation.root

import ru.vafeen.presentation.navigation.Screen

/**
 * Запечатанный интерфейс, представляющий все возможные намерения (интенты),
 * которые могут быть отправлены в [NavRootViewModel].
 */
internal sealed interface NavRootIntent {
    /**
     * Интент для проверки статуса аутентификации пользователя.
     */
    data object CheckAuth : NavRootIntent

    /**
     * Интент для проверки наличия обновлений приложения.
     */
    data object CheckUpdates : NavRootIntent

    /**
     * Интент для запуска процесса обновления приложения.
     */
    data object UpdateApp : NavRootIntent

    /**
     * Интент для навигации на экран настроек.
     */
    data object NavigateToSettings : NavRootIntent

    /**
     * Интент для навигации на указанный экран.
     *
     * @property screen Целевой экран для навигации.
     */
    data class NavigateTo(val screen: Screen) : NavRootIntent

    /**
     * Интент для замены всего стека навигации новым экраном.
     *
     * @property screen Новый корневой экран.
     */
    data class ReplaceRoot(val screen: Screen) : NavRootIntent

    /**
     * Интент для выполнения навигации назад.
     */
    data object NavigateBack : NavRootIntent
}
