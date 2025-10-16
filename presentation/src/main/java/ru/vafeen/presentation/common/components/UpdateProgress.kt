package ru.vafeen.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize

@Composable
internal fun UpdateProgress(percentage: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(AppTheme.colors.buttonColor),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextForThisTheme(
            modifier = Modifier.padding(vertical = 3.dp),
            text = "${stringResource(id = R.string.updating)} ${(percentage * 100).toInt()}%",
            fontSize = FontSize.medium19
        )
        LinearProgressIndicator(
            color = AppTheme.colors.text,
            trackColor = AppTheme.colors.background,
            progress = { percentage },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

