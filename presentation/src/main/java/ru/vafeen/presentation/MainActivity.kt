package ru.vafeen.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.vafeen.presentation.root.NavRoot
import ru.vafeen.presentation.ui.theme.MainTheme

/**
 * Главная Activity приложения, служащая точкой входа в UI.
 *
 * Эта Activity использует Jetpack Compose для построения интерфейса
 * и Hilt для внедрения зависимостей.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Вызывается при создании Activity.
     *
     * Устанавливает контент с помощью Jetpack Compose, применяя
     * основную тему [MainTheme] и корневой навигационный компонент [NavRoot].
     *
     * @param savedInstanceState Если Activity повторно инициализируется после
     * предыдущего уничтожения, этот Bundle содержит данные, которые она
     * в последний раз предоставила в onSaveInstanceState. В противном случае он равен null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MainTheme {
                NavRoot()
            }
        }
    }
}
