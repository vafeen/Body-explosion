package ru.vafeen.presentation.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.vafeen.domain.models.WorkoutUI
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize

@Composable
internal fun WorkoutUI.WorkoutString(onClick: (() -> Unit)?) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.buttonColor,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .let {
                    if (onClick != null) it.clickable { onClick() } else it
                }
                .padding(5.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextForThisTheme(
                    text = this@WorkoutString.text,
                    fontSize = FontSize.medium19,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextForThisTheme(
                    text = stringResource(R.string.description),
                    fontSize = FontSize.small17
                )
                TextForThisTheme(
                    text = this@WorkoutString.description,
                    fontSize = FontSize.small17,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextForThisTheme(
                    text = stringResource(R.string.duration),
                    fontSize = FontSize.small17
                )
                TextForThisTheme(
                    text = this@WorkoutString.duration,
                    fontSize = FontSize.small17,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}