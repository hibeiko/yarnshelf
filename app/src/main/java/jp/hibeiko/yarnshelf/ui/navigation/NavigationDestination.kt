package jp.hibeiko.yarnshelf.ui.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.navigation.NavType
import jp.hibeiko.yarnshelf.R
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// 画面遷移のための画面情報定義（インターフェース）
interface NavigationDestination {
    // 画面パス
    val route: String

    // 画面名
    val title: String
}

// 画面遷移時にカスタムNavTypeを渡すための設定
@Serializable
@Parcelize
data class YarnDataForScreen(
    val yarnId: Int = 0,
    val janCode: String = "",
    val yarnName: String = "",
    val yarnDescription: String = "",
//    val lastUpdateDate: Date = Date(),
    val imageUrl: String = "",
    @DrawableRes val drawableResourceId: Int = R.drawable.not_found
) : Parcelable

val YarnDataForScreenType = object : NavType<YarnDataForScreen>(
    isNullableAllowed = false
) {
    override fun put(bundle: Bundle, key: String, value: YarnDataForScreen) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: Bundle, key: String): YarnDataForScreen? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): YarnDataForScreen {
        return Json.decodeFromString<YarnDataForScreen>(value)
    }

    // Only required when using Navigation 2.4.0-alpha07 and lower
    override val name = "YarnDataForScreen"
}