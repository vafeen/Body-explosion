package ru.vafeen.presentation.common.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import ru.vafeen.presentation.ui.theme.AppTheme

@Composable
internal fun OutlinedTextFieldForThisTheme(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = AppTheme.colors.text,
            unfocusedTextColor = AppTheme.colors.text,
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
    )
}