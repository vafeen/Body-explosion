package ru.vafeen.presentation.common.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build


/**
 * Получает пару из versionCode и versionName текущего приложения.
 *
 * @param context Контекст приложения.
 * @return [Pair] где первый элемент — versionCode (Int?),
 * а второй — versionName (String?).
 */
fun Context.getAppVersion(): Pair<Int?, String?> {
    return try {
        val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionCode: Int? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode.toInt()
        } else {
            packageInfo.versionCode
        }
        val versionName: String? = packageInfo.versionName
        Pair(versionCode, versionName)
    } catch (e: Exception) {
        Pair(null, null)
    }
}