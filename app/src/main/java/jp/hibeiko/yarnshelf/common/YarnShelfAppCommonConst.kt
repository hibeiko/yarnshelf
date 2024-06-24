package jp.hibeiko.yarnshelf.common

// 毛糸：巻き方
enum class YarnRoll(val value: String) {
    // 玉巻
    BALL(value = "玉巻"),

    // かせ
    SKEIN(value = "かせ"),

    // 未設定
    NONE(value = "")
}

// 毛糸：太さ
enum class YarnThickness(val value: String, val rowValue: Int) {
    VERY_THIN(value = "極細", rowValue = 1),
    THIN(value = "合細", rowValue = 2),
    MIDDLE_THIN(value = "中細", rowValue = 3),
    THICK(value = "合太", rowValue = 4),
    NORMAL_THICK(value = "並太", rowValue = 5),
    VERY_THICK(value = "極太", rowValue = 6),
    VERY_VERY_THICK(value = "超極太", rowValue = 7),

    // 未設定
    NONE(value = "", rowValue = 0)
}

// YarnDataのどの項目かを識別するためのコード値
// maxLength...項目に設定可能な最大値（文字列なら文字数、数値なら最大値）
enum class YarnParamName( val maxLength: Int ) {
    // KEY * 必須項目
    YARN_ID(maxLength = 0),

    // JANコード
    JAN_CODE(maxLength = 13),

    // 名称 * 必須項目
    YARN_NAME(maxLength = 200),

    // メーカー
    YARN_MAKER_NAME(maxLength = 100),

    // 色番号
    COLOR_NUMBER(maxLength = 100),

    // ロット番号
    ROT_NUMBER(maxLength = 100),

    // 品質
    QUALITY(maxLength = 100),

    // 重量
    WEIGHT(maxLength = 10000),

    // 巻き方
    ROLL(maxLength = 0),

    // 長さ
    LENGTH(maxLength = 10000),

    // 標準ゲージ
    // 目 * Toに値を設定する場合はFromは必須
    GAUGE_COLUMN_FROM(maxLength = 100),
    GAUGE_COLUMN_TO(maxLength = 100),

    // 段 * Toに値を設定する場合はFromは必須
    GAUGE_ROW_FROM(maxLength = 100),
    GAUGE_ROW_TO(maxLength = 100),

    // 編み方
    GAUGE_STITCH(maxLength = 100),

    // 使用針
    // 棒針 * Toに値を設定する場合はFromは必須
    NEEDLE_SIZE_FROM(maxLength = 30),
    NEEDLE_SIZE_TO(maxLength = 30),

    // かぎ針 * Toに値を設定する場合はFromは必須
    CROCHET_NEEDLE_SIZE_FROM(maxLength = 30),
    CROCHET_NEEDLE_SIZE_TO(maxLength = 30),

    // 糸の太さ
    THICKNESS(maxLength = 0),

    // 個数 * 必須項目
    HAVING_NUMBER(maxLength = 1000),

    // 備考
    YARN_DESCRIPTION(maxLength = 1000),

    // 最終更新日
    LAST_UPDATE_DATE(maxLength = 0),

    // Yahooショッピングサイトの画像URL
    IMAGE_URL(maxLength = 0),

    // 端末に保存した画像ファイルパス
    DRAWABLE_RESOURCE_ID(maxLength = 0),
}