package ru.vafeen.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Класс данных, представляющий цвета темы приложения.
 *
 * @property mainColor Основной цвет акцента.
 * @property background Цвет фона приложения.
 * @property defaultButtonColor Цвет кнопки по умолчанию.
 * @property text Цвет текста.
 * @property buttonColor Цвет для специальных кнопок.
 */
internal data class AppThemeColors(
    val mainColor: Color,
    val background: Color,
    val defaultButtonColor: Color,
    val text: Color,
    val buttonColor: Color,
)

/**
 * Светлая палитра цветов.
 */
private val basePalette = AppThemeColors(
    mainColor = Color(0xFFECEA0E),
    background = Color.White,
    text = Color.Black,
    defaultButtonColor = Color.LightGray,
    buttonColor = Color(0xFFF9F9F9)
)

/**
 * Темная палитра цветов.
 */
private val baseDarkPalette = basePalette.copy(
    background = Color.Black,
    text = Color.White,
    defaultButtonColor = Color.DarkGray,
    buttonColor = Color(0xFF2D2D31)
)

/**
 * Основная тема приложения.
 *
 * @param darkTheme Использовать ли темную тему. По умолчанию используется системная настройка.
 * @param content Контент, к которому применяется тема.
 */
@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {

    val colors = if (darkTheme) {
        baseDarkPalette
    } else {
        basePalette
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        content = content
    )
}

/**
 * Объект для доступа к цветам текущей темы.
 */
internal object AppTheme {
    /**
     * Цвета текущей темы.
     */
    val colors: AppThemeColors
        @Composable
        get() = LocalColors.current
}

/**
 * CompositionLocal для передачи цветов темы в иерархии Composable.
 */
private val LocalColors = staticCompositionLocalOf<AppThemeColors> {
    error("Composition error")
}
