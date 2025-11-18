package ru.vafeen.presentation.root

import ru.vafeen.presentation.navigation.Screen

/**
 * Побочные эффекты, которые [NavRootViewModel] может отправить на UI для управления навигацией.
 */
internal sealed interface NavRootEffect {
    /**
     * Эффект для навигации на указанный экран.
     *
     * @property screen Целевой экран для навигации.
     */
    data class NavigateTo(val screen: Screen) : NavRootEffect

    /**
     * Эффект для замены всего стека навигации новым экраном.
     *
     * @property screen Новый корневой экран.
     */
    data class ReplaceRoot(val screen: Screen) : NavRootEffect

    /**
     * Эффект для выполнения навигации назад.
     */
    data object NavigateBack : NavRootEffect
}
