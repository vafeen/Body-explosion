package ru.vafeen.presentation.features.user_sign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.vafeen.presentation.common.components.OutlinedTextFieldForThisTheme
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.ui.theme.FontSize

/**
 * Экран входа и регистрации пользователя.
 *
 * @param onAuthSuccess Лямбда-функция, вызываемая при успешной аутентификации для навигации на следующий экран.
 * @param viewModel ViewModel, управляющая логикой экрана.
 */
@Composable
internal fun UserSignScreen(
    onAuthSuccess: () -> Unit,
    viewModel: UserSignViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is UserSignEffect.AuthSuccess -> onAuthSuccess()
            }
        }
    }

    UserSignContent(
        state = state,
        onIntent = viewModel::handleIntent
    )
}

/**
 * Контент экрана входа/регистрации.
 *
 * @param state Текущее состояние экрана.
 * @param onIntent Функция для отправки интентов в ViewModel.
 */
@Composable
private fun UserSignContent(
    state: UserSignState,
    onIntent: (UserSignIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextForThisTheme(
            text = if (state.isSignUp) "Регистрация" else "Вход",
            fontSize = FontSize.big22
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isSignUp) {
            OutlinedTextFieldForThisTheme(
                value = state.name,
                onValueChange = { onIntent(UserSignIntent.OnNameChanged(it)) },
                label = { TextForThisTheme(text = "Имя", fontSize = FontSize.small17) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextFieldForThisTheme(
            value = state.username,
            onValueChange = { onIntent(UserSignIntent.OnUsernameChanged(it)) },
            label = { TextForThisTheme(text = "Имя пользователя", fontSize = FontSize.small17) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextFieldForThisTheme(
            value = state.password,
            onValueChange = { onIntent(UserSignIntent.OnPasswordChanged(it)) },
            label = { TextForThisTheme(text = "Пароль", fontSize = FontSize.small17) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        if (state.error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { onIntent(UserSignIntent.Submit) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (state.isSignUp) "Зарегистрироваться" else "Войти",
                    fontSize = FontSize.small17,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { onIntent(UserSignIntent.ToggleMode) }) {
            TextForThisTheme(
                text = if (state.isSignUp) "Уже есть аккаунт? Войти"
                else "Нет аккаунта? Зарегистрироваться",
                fontSize = FontSize.small17
            )
        }
    }
}
