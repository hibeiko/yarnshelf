package jp.naorin.yarnshelf.ui.navigation

import androidx.annotation.DrawableRes
import jp.naorin.yarnshelf.R
import jp.naorin.yarnshelf.common.YarnRoll
import jp.naorin.yarnshelf.common.YarnThickness

// 画面遷移のための画面情報定義（インターフェース）
interface NavigationDestination {
    // 画面パス
    val route: String

    // 画面名
    val title: String
}

// 画面表示用のデータクラス
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
    val weight: String = "",
    // 巻き方
    val roll: YarnRoll = YarnRoll.NONE,
    // 長さ
    val length: String = "",
    // 標準ゲージ
    // 目
    val gaugeColumnFrom: String = "",
    val gaugeColumnTo: String = "",
    // 段
    val gaugeRowFrom: String = "",
    val gaugeRowTo: String = "",
    // 編み方
    val gaugeStitch: String = "",
    // 使用針
    // 棒針
    val needleSizeFrom: String = "",
    val needleSizeTo: String = "",
    // かぎ針
    val crochetNeedleSizeFrom: String = "",
    val crochetNeedleSizeTo: String = "",
    // 糸の太さ
    val thickness: YarnThickness = YarnThickness.NONE,
    // 個数
    val havingNumber: String = "0",
    // 備考
    val yarnDescription: String = "",
    // 最終更新日
//    val lastUpdateDate: Date = Date(),
    // Yahooショッピングサイトの画像URL
    val imageUrl: String = "",
    // 端末に保存した画像ファイルパス
    @DrawableRes val drawableResourceId: Int = R.drawable.not_found
)
