package ru.vafeen.presentation.common.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import ru.vafeen.presentation.ui.theme.AppTheme

@Composable
fun TextForThisTheme(modifier: Modifier = Modifier, text: String, fontSize: TextUnit) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        color = AppTheme.colors.text,
        textAlign = TextAlign.Center
    )
}