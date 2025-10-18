package ru.vafeen.data.datastore

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import ru.vafeen.data.datastore.converters.toDataStoreSettings
import ru.vafeen.data.datastore.converters.toSettings
import ru.vafeen.data.datastore.models.DataStoreSettings
import ru.vafeen.domain.datastore.SettingsManager
import ru.vafeen.domain.models.Settings
import javax.inject.Inject

/**
 * Реализация интерфейса [SettingsManager] для управления настройками приложения с использованием [SharedPreferences].
 * Этот класс предоставляет реактивный способ наблюдения за изменениями настроек через [StateFlow].
 *
 * @property sharedPreferences Экземпляр [SharedPreferences], используемый для хранения и загрузки настроек.
 */
internal class SettingsManagerImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    SettingsManager {

    /**
     * Внутренний StateFlow для отслеживания изменений настроек.
     */
    private val _settingsFlow =
        MutableStateFlow(sharedPreferences.getSettingsOrCreateIfNull().toSettings())

    /**
     * Публичный [StateFlow] для подписки на изменения настроек.
     * С помощью этого потока можно получать уведомления об обновлениях настроек.
     */
    override val settingsFlow: StateFlow<Settings> = _settingsFlow.asStateFlow()

    /**
     * Сохраняет настройки, применяя функцию преобразования к текущим настройкам.
     * Обновленные настройки сохраняются в [SharedPreferences] и автоматически передаются
     * подписчикам через [StateFlow].
     *
     * @param saving Функция, которая принимает текущие настройки и возвращает обновленные.
     */
    @Synchronized
    override fun save(saving: (Settings) -> Settings) {
        // Обновляем настройки в памяти
        val newSettings = saving(_settingsFlow.value)
        // Сохраняем обновленные настройки в SharedPreferences
        saveSettingsToSharedPreferences(newSettings.toDataStoreSettings())
        // Обновляем flow
        _settingsFlow.update { newSettings }
    }


    private fun saveSettingsToSharedPreferences(dataStoreSettings: DataStoreSettings) {
        sharedPreferences.saveInOrRemoveFromSharedPreferences {
            putString(SETTINGS_KEY, dataStoreSettings.toJsonString())
        }
    }

    /**
     * Получает настройки из SharedPreferences или создает новые, если их нет.
     * Эта функция проверяет наличие настроек в SharedPreferences и возвращает их.
     * Если настройки не найдены, создаются новые и сохраняются в SharedPreferences.
     *
     * @return Объект [DataStoreSettings], полученный из SharedPreferences или созданный по умолчанию.
     */
    private fun SharedPreferences.getSettingsOrCreateIfNull(): DataStoreSettings {
        val settings = getFromSharedPreferences {
            getString(SETTINGS_KEY, "").let {
                if (it != "") it?.decodeDatastoreSettings()
                else null
            }
        }
        return settings ?: DataStoreSettings()
            .also {
                saveInOrRemoveFromSharedPreferences {
                    putString(SETTINGS_KEY, it.toJsonString())
                }
            }
    }

    /**
     * Сохраняет данные в SharedPreferences или удаляет их, в зависимости от переданного блока.
     *
     * Эта функция позволяет выполнить операции редактирования SharedPreferences,
     * используя переданный блок кода.
     *
     * @param save Блок кода, который будет выполнен в контексте [SharedPreferences.Editor].
     */
    private fun SharedPreferences.saveInOrRemoveFromSharedPreferences(save: SharedPreferences.Editor.() -> Unit) {
        edit().apply {
            save() // Выполнение переданного блока.
            apply() // Применение изменений.
        }
    }

    /**
     * Получает данные из SharedPreferences с использованием переданного блока.
     *
     * @param get Блок кода, который будет выполнен в контексте [SharedPreferences].
     * @return Результат выполнения блока.
     */
    private fun <T> SharedPreferences.getFromSharedPreferences(get: SharedPreferences.() -> T): T =
        get()

    private fun DataStoreSettings.toJsonString(): String = Json.encodeToString(this)
    private fun String.decodeDatastoreSettings(): DataStoreSettings = Json.decodeFromString(this)

    companion object {
        private const val SETTINGS_KEY = "SETTINGS_KEY"
        const val SETTINGS_SHARED_PREFERENCES = "SETTINGS_SHARED_PREFERENCES"
    }

}