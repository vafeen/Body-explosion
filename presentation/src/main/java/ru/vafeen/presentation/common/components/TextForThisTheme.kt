package ru.vafeen.presentation.common.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Composable-функция для отображения текста с использованием основного цвета текста из темы [AppTheme].
 *
 * @param modifier Модификатор для настройки макета.
 * @param text Текст для отображения.
 * @param fontSize Размер шрифта.
 * @param color Цвет текста. По умолчанию используется основной цвет текста из темы.
 */
@Composable
internal fun TextForThisTheme(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit,
    color: Color = AppTheme.colors.text,
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        color = color,
        textAlign = TextAlign.Center
    )
}
