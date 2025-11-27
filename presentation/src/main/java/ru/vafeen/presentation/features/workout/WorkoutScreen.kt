package ru.vafeen.presentation.features.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.vafeen.domain.models.WorkoutUI
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.common.components.WorkoutString
import ru.vafeen.presentation.navigation.Screen
import ru.vafeen.presentation.root.NavRootIntent
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize

val target = "Силовая тренировка"

@Composable
internal fun WorkoutScreen(
    sendRootIntent: (NavRootIntent) -> Unit,
    viewModel: WorkoutViewModel = hiltViewModel<WorkoutViewModel, WorkoutViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(sendRootIntent)
        }
    )
) {


    val list = listOf(
        WorkoutUI(
            target,
            "Упражнения с собственным весом для всех групп мышц",
            "20 минут 10 секунд"
        ),
        WorkoutUI(
            "Утренняя зарядка",
            "Базовые упражнения для пробуждения и тонуса",
            "5 минут 15 секунд"
        ),
        WorkoutUI(
            "Интервальный бег",
            "Чередование быстрого и медленного темпа",
            "12 минут 30 секунд"
        ),
        WorkoutUI(
            "Йога для начинающих",
            "Основные асаны для гибкости и расслабления",
            "15 минут 45 секунд"
        ),
        WorkoutUI(
            "Кардио-комплекс",
            "Высокоинтенсивные упражнения для сжигания калорий",
            "18 минут 25 секунд"
        ),
        WorkoutUI(
            "Растяжка",
            "Упражнения для улучшения гибкости и восстановления",
            "8 минут 40 секунд"
        ),
        WorkoutUI(
            "Тренировка пресса",
            "Упражнения для укрепления мышц кора",
            "10 минут 20 секунд"
        ),
        WorkoutUI(
            "Функциональный тренинг",
            "Упражнения для повседневной активности",
            "25 минут 15 секунд"
        ),
        WorkoutUI(
            "Пилатес",
            "Упражнения для осанки и глубоких мышц",
            "22 минуты 50 секунд"
        ),
        WorkoutUI(
            "Восстановительная тренировка",
            "Легкие упражнения после интенсивных нагрузок",
            "7 минут 35 секунд"
        )
    )

    // на этом скрине высвечивать все тренировки которые есть и по нажатию на иконку начала - начинать
    //// сделать кнопку добавления тренировки

    // и кликабельное будет только одно
    Scaffold(
        containerColor = AppTheme.colors.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = AppTheme.colors.mainColor,
                contentColor = AppTheme.colors.text,
            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        it
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { viewModel.handleIntent(WorkoutIntent.NavigateToHistory) }) {
                    Icon(
                        modifier = Modifier,
                        painter = painterResource(R.drawable.history),
                        contentDescription = stringResource(R.string.history),
                        tint = AppTheme.colors.text
                    )
                }
                TextForThisTheme(
                    text = "Тренировки",
                    fontSize = FontSize.big22
                )
                IconButton(
                    onClick = { viewModel.handleIntent(WorkoutIntent.NavigateToSettings) }) {
                    Icon(
                        modifier = Modifier,
                        painter = painterResource(R.drawable.settings),
                        contentDescription = stringResource(R.string.settings),
                        tint = AppTheme.colors.text
                    )
                }
            }


            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(1.dp))
                }
                items(list) { workoutUi ->
                    workoutUi.WorkoutString(
                        onClick = if (workoutUi.text == target) {
                            {
                                sendRootIntent(
                                    NavRootIntent.NavigateTo(
                                        Screen.Training
                                    )
                                )
                            }
                        } else null
                    )
                }
            }
        }
    }
}