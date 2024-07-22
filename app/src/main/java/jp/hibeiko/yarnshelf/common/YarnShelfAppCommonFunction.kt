package jp.hibeiko.yarnshelf.common

import jp.hibeiko.yarnshelf.data.YahooHit
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.navigation.YarnDataForScreen
import java.text.DecimalFormat

// 標準状態重量（画面表示用）文字列
fun formatWeightStringForScreen(weight: Double?, length: Double?, roll: YarnRoll): String {
    val strWeight = if (weight != null) "${DecimalFormat("#.#").format(weight)}g" else "-"
    val strRoll = if (roll != YarnRoll.NONE) roll.value else ""
    val strLength = if (length != null) "(糸長約${DecimalFormat("#.#").format(length)}m)" else ""
    // 10.4g玉巻(糸長約105.5m)
    return strWeight + strRoll + strLength
}
// 標準ゲージ（画面表示用）文字列

fun formatGaugeStringForScreen(
    gaugeColumnFrom: Double?,
    gaugeColumnTo: Double?,
    gaugeRowFrom: Double?,
    gaugeRowTo: Double?,
    gaugeStitch: String?
): String {
    // 小数点1桁、もしくは整数
    val format = DecimalFormat("#.#")
    val strGaugeColumn =
        if (gaugeColumnFrom != null) {
            format.format(gaugeColumnFrom) +
                    if (gaugeColumnTo != null)
                        "-${format.format(gaugeColumnTo)}目"
                    else "目"
        } else "-"
    val strGaugeRow =
        if (gaugeRowFrom != null) {
            format.format(gaugeRowFrom) +
                    if (gaugeRowTo != null)
                        "-${format.format(gaugeRowTo)}段"
                    else "段"
        } else ""

    // 20-21目27-28段(模様編み)
    // メリヤス編みの場合は省略する
    val strGaugeStitch =
        if (!gaugeStitch.isNullOrBlank() && "メリヤス" !in gaugeStitch) "(${gaugeStitch})" else ""
    return strGaugeColumn + strGaugeRow + strGaugeStitch
}

// 参考使用針（画面表示用）文字列
fun formatNeedleSizeStringForScreen(
    needleSizeFrom: Double?,
    needleSizeTo: Double?,
    crochetNeedleSizeFrom: Double?,
    crochetNeedleSizeTo: Double?
): String {
    // 小数点1桁、もしくは整数
    val format = DecimalFormat("#.#")
    val strNeedleSize =
        if (needleSizeFrom != null) {
            "棒針${format.format(needleSizeFrom)}" +
                    if (needleSizeTo != null)
                        "-${format.format(needleSizeTo)}号"
                    else "号"
        } else ""
    val strCrochetNeedleSize =
        if (crochetNeedleSizeFrom != null) {
            "かぎ針${format.format(crochetNeedleSizeFrom)}" +
                    if (crochetNeedleSizeTo != null)
                        "-${format.format(crochetNeedleSizeTo)}号"
                    else "号"
        } else ""

    // 棒針8-10号
    // かぎ針8-10号
    return if (strNeedleSize.isNotBlank() && strCrochetNeedleSize.isNotBlank())
        "$strNeedleSize\n$strCrochetNeedleSize"
    else if (strNeedleSize.isBlank() && strCrochetNeedleSize.isBlank())
        "-"
    else
        "$strNeedleSize$strCrochetNeedleSize"

}


fun updateYarnData(
    yarnData: YarnDataForScreen,
    param: Any,
    paramName: YarnParamName
): YarnDataForScreen {

    return when (paramName) {
        YarnParamName.JAN_CODE
        -> yarnData.copy(janCode = param as String)

        YarnParamName.YARN_MAKER_NAME
        -> yarnData.copy(yarnMakerName = param as String)

        YarnParamName.YARN_NAME
        -> yarnData.copy(yarnName = param as String)

        YarnParamName.IMAGE_URL
        -> yarnData.copy(imageUrl = param as String)

        YarnParamName.HAVING_NUMBER
        -> yarnData.copy(havingNumber = (param as String).ifBlank { "0" })

        YarnParamName.COLOR_NUMBER
        -> yarnData.copy(colorNumber = param as String)

        YarnParamName.ROT_NUMBER
        -> yarnData.copy(rotNumber = param as String)

        YarnParamName.QUALITY
        -> yarnData.copy(quality = param as String)

        YarnParamName.WEIGHT
        -> yarnData.copy(weight = param as String)

        YarnParamName.ROLL
        -> yarnData.copy(roll = param as YarnRoll)

        YarnParamName.LENGTH
        -> yarnData.copy(length = param as String)

        YarnParamName.GAUGE_COLUMN_FROM
        -> yarnData.copy(gaugeColumnFrom = param as String)

        YarnParamName.GAUGE_COLUMN_TO
        -> yarnData.copy(gaugeColumnTo = param as String)

        YarnParamName.GAUGE_ROW_FROM
        -> yarnData.copy(gaugeRowFrom = param as String)

        YarnParamName.GAUGE_ROW_TO
        -> yarnData.copy(gaugeRowTo = param as String)

        YarnParamName.GAUGE_STITCH
        -> yarnData.copy(gaugeStitch = param as String)

        YarnParamName.NEEDLE_SIZE_FROM
        -> yarnData.copy(needleSizeFrom = param as String)

        YarnParamName.NEEDLE_SIZE_TO
        -> yarnData.copy(needleSizeTo = param as String)

        YarnParamName.CROCHET_NEEDLE_SIZE_FROM
        -> yarnData.copy(crochetNeedleSizeFrom = param as String)

        YarnParamName.CROCHET_NEEDLE_SIZE_TO
        -> yarnData.copy(crochetNeedleSizeTo = param as String)

        YarnParamName.THICKNESS
        -> yarnData.copy(thickness = param as YarnThickness)

        YarnParamName.YARN_DESCRIPTION
        -> yarnData.copy(yarnDescription = param as String)
    }
}

fun yarnDataToYarnDataForScreenConverter(yarnData: YarnData): YarnDataForScreen {
    return YarnDataForScreen(
        yarnId = yarnData.yarnId,
        janCode = yarnData.janCode,
        yarnName = yarnData.yarnName,
        yarnMakerName = yarnData.yarnMakerName,
        colorNumber = yarnData.colorNumber,
        rotNumber = yarnData.rotNumber,
        quality = yarnData.quality,
        weight = DecimalFormat("#.#").format(yarnData.weight),
        roll = yarnData.roll,
        length = DecimalFormat("#.#").format(yarnData.length),
        gaugeColumnFrom = DecimalFormat("#.#").format(yarnData.gaugeColumnFrom),
        gaugeColumnTo = DecimalFormat("#.#").format(yarnData.gaugeColumnTo),
        gaugeRowFrom = DecimalFormat("#.#").format(yarnData.gaugeRowFrom),
        gaugeRowTo = DecimalFormat("#.#").format(yarnData.gaugeRowTo),
        gaugeStitch = yarnData.gaugeStitch,
        needleSizeFrom = DecimalFormat("#.#").format(yarnData.needleSizeFrom),
        needleSizeTo = DecimalFormat("#.#").format(yarnData.needleSizeTo),
        crochetNeedleSizeFrom = DecimalFormat("#.#").format(yarnData.crochetNeedleSizeFrom),
        crochetNeedleSizeTo = DecimalFormat("#.#").format(yarnData.crochetNeedleSizeTo),
        thickness = yarnData.thickness,
        havingNumber = DecimalFormat("#.#").format(yarnData.havingNumber),
        yarnDescription = yarnData.yarnDescription,
        imageUrl = yarnData.imageUrl,
        )
}

fun yarnDataForScreenToYarnDataConverter(yarnDataForScreen: YarnDataForScreen): YarnData {
    return YarnData(
        yarnId = yarnDataForScreen.yarnId,
        janCode = yarnDataForScreen.janCode,
        yarnName = yarnDataForScreen.yarnName,
        yarnMakerName = yarnDataForScreen.yarnMakerName,
        colorNumber = yarnDataForScreen.colorNumber,
        rotNumber = yarnDataForScreen.rotNumber,
        quality = yarnDataForScreen.quality,
        weight = yarnDataForScreen.weight.ifBlank { null }?.toDouble(),
        roll = yarnDataForScreen.roll,
        length = yarnDataForScreen.length.ifBlank { null }?.toDouble(),
        gaugeColumnFrom = yarnDataForScreen.gaugeColumnFrom.ifBlank { null }?.toDouble(),
        gaugeColumnTo = yarnDataForScreen.gaugeColumnTo.ifBlank { null }?.toDouble(),
        gaugeRowFrom = yarnDataForScreen.gaugeRowFrom.ifBlank { null }?.toDouble(),
        gaugeRowTo = yarnDataForScreen.gaugeRowTo.ifBlank { null }?.toDouble(),
        gaugeStitch = yarnDataForScreen.gaugeStitch,
        needleSizeFrom = yarnDataForScreen.needleSizeFrom.ifBlank { null }?.toDouble(),
        needleSizeTo = yarnDataForScreen.needleSizeTo.ifBlank { null }?.toDouble(),
        crochetNeedleSizeFrom = yarnDataForScreen.crochetNeedleSizeFrom.ifBlank { null }
            ?.toDouble(),
        crochetNeedleSizeTo = yarnDataForScreen.crochetNeedleSizeTo.ifBlank { null }?.toDouble(),
        thickness = yarnDataForScreen.thickness,
        havingNumber = yarnDataForScreen.havingNumber.ifBlank { "0" }.toInt(),
        yarnDescription = yarnDataForScreen.yarnDescription,
        imageUrl = yarnDataForScreen.imageUrl,
    )
}

fun yahooHitToYarnDataForScreenConverter(yahooHit: YahooHit): YarnDataForScreen {
    return YarnDataForScreen(
        janCode = yahooHit.janCode ?: "",
        yarnName = yahooHit.name ?: "",
//        yarnMakerName = yahooHit.yarnMakerName,
//        colorNumber = yahooHit.colorNumber,
//        rotNumber = yahooHit.rotNumber,
//        quality = yahooHit.quality,
//        weight = yahooHit.weight,
//        roll = yahooHit.roll,
//        length = yahooHit.length,
//        gaugeColumnFrom = yahooHit.gaugeColumnFrom,
//        gaugeColumnTo = yahooHit.gaugeColumnTo,
//        gaugeRowFrom = yahooHit.gaugeRowFrom,
//        gaugeRowTo = yahooHit.gaugeRowTo,
//        gaugeStitch = yahooHit.gaugeStitch,
//        needleSizeFrom = yahooHit.needleSizeFrom,
//        needleSizeTo = yahooHit.needleSizeTo,
//        crochetNeedleSizeFrom = yahooHit.crochetNeedleSizeFrom,
//        crochetNeedleSizeTo = yahooHit.crochetNeedleSizeTo,
//        thickness = yahooHit.thickness,
        havingNumber = "0",
        yarnDescription = yahooHit.description ?: "",
        imageUrl = yahooHit.image.medium ?: "",
//        drawableResourceId = yahooHit.drawableResourceId,
    )
}

// バリデーションチェック
fun validateInput(yarnData: YarnDataForScreen, paramName: YarnParamName): String {
    return when (paramName) {
        YarnParamName.JAN_CODE
        ->
            if (yarnData.janCode.length > YarnParamName.JAN_CODE.maxLength)
                "${YarnParamName.JAN_CODE.maxLength}桁で入力してください。"
            else ""

        YarnParamName.YARN_MAKER_NAME
        ->
            if (yarnData.yarnMakerName.length > YarnParamName.YARN_MAKER_NAME.maxLength)
                "${YarnParamName.YARN_MAKER_NAME.maxLength}文字以内で入力してください。"
            else ""

        YarnParamName.YARN_NAME
        ->
            if (yarnData.yarnName.isEmpty())
                "名前は必須項目です。"
            else if (yarnData.yarnName.length > YarnParamName.YARN_NAME.maxLength)
                "${YarnParamName.YARN_NAME.maxLength}文字以内で入力してください。"
            else
                ""

        YarnParamName.IMAGE_URL
        -> ""

        YarnParamName.HAVING_NUMBER
        -> if (yarnData.havingNumber.isNotBlank())
            try {
                if (yarnData.havingNumber.toInt() > YarnParamName.HAVING_NUMBER.maxLength)
                    "${YarnParamName.HAVING_NUMBER.maxLength}個以内で入力してください。"
                else
                    ""
            } catch (e: NumberFormatException) {
                return "数値を入力してください。"
            }
        else
            "数量は必須項目です。"

        YarnParamName.COLOR_NUMBER
        -> if (yarnData.colorNumber.length > YarnParamName.COLOR_NUMBER.maxLength)
            "${YarnParamName.COLOR_NUMBER.maxLength}文字以内で入力してください。"
        else
            ""

        YarnParamName.ROT_NUMBER
        -> if (yarnData.rotNumber.length > YarnParamName.ROT_NUMBER.maxLength)
            "${YarnParamName.ROT_NUMBER.maxLength}文字以内で入力してください。"
        else
            ""

        YarnParamName.QUALITY
        -> if (yarnData.quality.length > YarnParamName.QUALITY.maxLength)
            "${YarnParamName.QUALITY.maxLength}文字以内で入力してください。"
        else
            ""

        YarnParamName.WEIGHT
        -> if (yarnData.weight.isNotBlank())
            try {
                if (yarnData.weight.toDouble() > YarnParamName.WEIGHT.maxLength.toDouble())
                    "${YarnParamName.WEIGHT.maxLength}g以内で入力してください。"
                else
                    ""
            } catch (e: NumberFormatException) {
                return "数値を入力してください。"
            }
        else
            ""

        YarnParamName.ROLL
        -> if (yarnData.roll in YarnRoll.entries)
            ""
        else
            "値が不正です。"

        YarnParamName.LENGTH
        -> if (yarnData.length.isNotBlank())
            try {
                if (yarnData.length.toDouble() > YarnParamName.LENGTH.maxLength.toDouble())
                    "${YarnParamName.LENGTH.maxLength}m以内で入力してください。"
                else
                    ""
            } catch (e: NumberFormatException) {
                return "数値を入力してください。"
            }
        else
            ""

        YarnParamName.GAUGE_COLUMN_FROM
        -> if (yarnData.gaugeColumnFrom.isNotBlank())
            try {
                if (yarnData.gaugeColumnFrom.toDouble() > YarnParamName.GAUGE_COLUMN_FROM.maxLength.toDouble())
                    "${YarnParamName.GAUGE_COLUMN_FROM.maxLength}以内で入力してください。"
                else
                    ""
            } catch (e: NumberFormatException) {
                return "数値を入力してください。"
            }
        else
            ""

        YarnParamName.GAUGE_COLUMN_TO
        -> if (yarnData.gaugeColumnTo.isNotBlank())
            try {
                if (yarnData.gaugeColumnTo.toDouble() > YarnParamName.GAUGE_COLUMN_TO.maxLength.toDouble())
                    "${YarnParamName.GAUGE_COLUMN_TO.maxLength}以内で入力してください。"
                else
                    ""
            } catch (e: NumberFormatException) {
                return "数値を入力してください。"
            }
        else
            ""

        YarnParamName.GAUGE_ROW_FROM
        -> if (yarnData.gaugeRowFrom.isNotBlank())
            try {
                if (yarnData.gaugeRowFrom.toDouble() > YarnParamName.GAUGE_ROW_FROM.maxLength.toDouble())
                    "${YarnParamName.GAUGE_ROW_FROM.maxLength}以内で入力してください。"
                else
                    ""
            } catch (e: NumberFormatException) {
                return "数値を入力してください。"
            }
        else
            ""

        YarnParamName.GAUGE_ROW_TO
        -> if (yarnData.gaugeRowTo.isNotBlank())
            try {
                if (yarnData.gaugeRowTo.toDouble() > YarnParamName.GAUGE_ROW_TO.maxLength.toDouble())
                    "${YarnParamName.GAUGE_ROW_TO.maxLength}以内で入力してください。"
                else
                    ""
            } catch (e: NumberFormatException) {
                return "数値を入力してください。"
            }
        else
            ""

        YarnParamName.GAUGE_STITCH
        -> if (yarnData.gaugeStitch.length > YarnParamName.GAUGE_STITCH.maxLength)
            "${YarnParamName.GAUGE_STITCH.maxLength}文字以内で入力してください。"
        else
            ""

        YarnParamName.NEEDLE_SIZE_FROM
        -> if (yarnData.needleSizeFrom.isNotBlank())
            try {
                if (yarnData.needleSizeFrom.toDouble() > YarnParamName.NEEDLE_SIZE_FROM.maxLength.toDouble())
                    "${YarnParamName.NEEDLE_SIZE_FROM.maxLength}以内で入力してください。"
                else
                    ""
            } catch (e: NumberFormatException) {
                return "数値を入力してください。"
            }
        else
            ""

        YarnParamName.NEEDLE_SIZE_TO
        -> if (yarnData.needleSizeTo.isNotBlank())
            try {
                if (yarnData.needleSizeTo.toDouble() > YarnParamName.NEEDLE_SIZE_TO.maxLength.toDouble())
                    "${YarnParamName.NEEDLE_SIZE_TO.maxLength}以内で入力してください。"
                else
                    ""
            } catch (e: NumberFormatException) {
                return "数値を入力してください。"
            }
        else
            ""

        YarnParamName.CROCHET_NEEDLE_SIZE_FROM
        -> if (yarnData.crochetNeedleSizeFrom.isNotBlank())
            try {
                if (yarnData.crochetNeedleSizeFrom.toDouble() > YarnParamName.CROCHET_NEEDLE_SIZE_FROM.maxLength.toDouble())
                    "${YarnParamName.CROCHET_NEEDLE_SIZE_FROM.maxLength}以内で入力してください。"
                else
                    ""
            } catch (e: NumberFormatException) {
                return "数値を入力してください。"
            }
        else
            ""

        YarnParamName.CROCHET_NEEDLE_SIZE_TO
        -> if (yarnData.crochetNeedleSizeTo.isNotBlank())
            try {
                if (yarnData.crochetNeedleSizeTo.toDouble() > YarnParamName.CROCHET_NEEDLE_SIZE_TO.maxLength.toDouble())
                    "${YarnParamName.CROCHET_NEEDLE_SIZE_TO.maxLength}以内で入力してください。"
                else
                    ""
            } catch (e: NumberFormatException) {
                return "数値を入力してください。"
            }
        else
            ""

        YarnParamName.THICKNESS
        -> if (yarnData.thickness in YarnThickness.entries)
            ""
        else
            "値が不正です。"

        YarnParamName.YARN_DESCRIPTION
        -> if (yarnData.yarnDescription.length > YarnParamName.YARN_DESCRIPTION.maxLength)
            "${YarnParamName.YARN_DESCRIPTION.maxLength}文字以内で入力してください。"
        else
            ""
    }
}
