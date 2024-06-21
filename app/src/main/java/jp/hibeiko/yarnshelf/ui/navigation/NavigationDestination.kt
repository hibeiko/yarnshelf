package jp.hibeiko.yarnshelf.ui.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.navigation.NavType
import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.common.YarnRoll
import jp.hibeiko.yarnshelf.common.YarnThickness
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
    // JANコード
    val janCode: String = "",
    // 名称
    val yarnName: String = "",
    // メーカー
    val yarnMakerName: String = "",
    // 色番号
    val colorNumber: String = "",
    // ロット番号
    val rotNumber: String = "",
    // 品質
    val quality: String = "",
    // 重量
    val weight: Double? = null,
    // 巻き方
    val roll: YarnRoll = YarnRoll.NONE,
    // 長さ
    val length: Double? = null,
    // 標準ゲージ
    // 目
    val gaugeColumnFrom: Double? = null,
    val gaugeColumnTo: Double? = null,
    // 段
    val gaugeRowFrom: Double? = null,
    val gaugeRowTo: Double? = null,
    // 編み方
    val gaugeStitch: String = "",
    // 使用針
    // 棒針
    val needleSizeFrom: Double? = null,
    val needleSizeTo: Double? = null,
    // かぎ針
    val crochetNeedleSizeFrom: Double? = null,
    val crochetNeedleSizeTo: Double? = null,
    // 糸の太さ
    val thickness: YarnThickness = YarnThickness.NONE,
    // 個数
    val havingNumber: Int = 0,
    // 備考
    val yarnDescription: String = "",
    // 最終更新日
//    val lastUpdateDate: Date = Date(),
    // Yahooショッピングサイトの画像URL
    val imageUrl: String = "",
    // 端末に保存した画像ファイルパス
    @DrawableRes val drawableResourceId: Int = R.drawable.not_found
) : Parcelable

val YarnDataForScreenType = object : NavType<YarnDataForScreen>(
    isNullableAllowed = false
) {
    override fun put(bundle: Bundle, key: String, value: YarnDataForScreen) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: Bundle, key: String): YarnDataForScreen? {
        return bundle.getParcelable(key, YarnDataForScreen::class.java)
    }

    override fun parseValue(value: String): YarnDataForScreen {
        return Json.decodeFromString<YarnDataForScreen>(value)
    }

    // Only required when using Navigation 2.4.0-alpha07 and lower
    override val name = "YarnDataForScreen"
}

