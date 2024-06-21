package jp.hibeiko.yarnshelf.common

// 毛糸：巻き方
enum class YarnRoll(val value: String) {
    // かせ
    SKEIN( value = "かせ"),
    // 玉巻
    BALL( value = "玉巻"),
    // 未設定
    NONE( value = "" )
}
// 毛糸：太さ
enum class YarnThickness (val value: String) {
    VERY_THIN( value = "極細"),
    THIN( value = "合細"),
    MIDDLE_THIN( value = "中細"),
    THICK( value = "合太"),
    NORMAL_THICK( value = "並太"),
    VERY_THICK( value = "極太"),
    VERY_VERY_THICK( value = "超極太"),
    // 未設定
    NONE( value = "" )
}
// YarnDataのどの項目かを識別するためのコード値
enum class YarnParamName {
    // KEY * 必須項目
    YARN_ID,

    // JANコード
    JAN_CODE,

    // 名称 * 必須項目
    YARN_NAME,

    // メーカー
    YARN_MAKER_NAME,

    // 色番号
    COLOR_NUMBER,

    // ロット番号
    ROT_NUMBER,

    // 品質
    QUALITY,

    // 重量
    WEIGHT,

    // 巻き方
    ROLL,

    // 長さ
    LENGTH,

    // 標準ゲージ
    // 目 * Toに値を設定する場合はFromは必須
    GAUGE_COLUMN_FROM,
    GAUGE_COLUMN_TO,

    // 段 * Toに値を設定する場合はFromは必須
    GAUGE_ROW_FROM,
    GAUGE_ROW_TO,

    // 編み方
    GAUGE_STITCH,

    // 使用針
    // 棒針 * Toに値を設定する場合はFromは必須
    NEEDLE_SIZE_FROM,
    NEEDLE_SIZE_TO,

    // かぎ針 * Toに値を設定する場合はFromは必須
    CROCHET_NEEDLE_SIZE_FROM,
    CROCHET_NEEDLE_SIZE_TO,

    // 糸の太さ
    THICKNESS,

    // 個数 * 必須項目
    HAVING_NUMBER,

    // 備考
    YARN_DESCRIPTION,

    // 最終更新日
    LAST_UPDATE_DATE,

    // Yahooショッピングサイトの画像URL
    IMAGE_URL,

    // 端末に保存した画像ファイルパス
    DRAWABLE_RESOURCE_ID
}