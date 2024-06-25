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
        } else "-"
    val strCrochetNeedleSize =
        if (crochetNeedleSizeFrom != null) {
            "\nかぎ針${format.format(crochetNeedleSizeFrom)}" +
                    if (crochetNeedleSizeTo != null)
                        "-${format.format(crochetNeedleSizeTo)}号"
                    else "号"
        } else ""

    // 棒針8-10号
    // かぎ針8-10号
    return "$strNeedleSize$strCrochetNeedleSize"
}


fun updateYarnData(yarnData: YarnData, param: Any, paramName: YarnParamName): YarnData {

    return when (paramName) {
        YarnParamName.JAN_CODE
        -> yarnData.copy(janCode = param as String)

        YarnParamName.YARN_MAKER_NAME
        -> yarnData.copy(yarnMakerName = param as String)

        YarnParamName.YARN_NAME
        -> yarnData.copy(yarnName = param as String)

        YarnParamName.IMAGE_URL
        -> yarnData.copy(imageUrl = param as String)

        YarnParamName.DRAWABLE_RESOURCE_ID
        -> yarnData.copy(drawableResourceId = (param as String).toInt())

        YarnParamName.HAVING_NUMBER
        -> yarnData.copy(havingNumber = ((param as String).ifBlank { "0" }).toInt())

        YarnParamName.COLOR_NUMBER
        -> yarnData.copy(colorNumber = param as String)

        YarnParamName.ROT_NUMBER
        -> yarnData.copy(rotNumber = param as String)

        YarnParamName.QUALITY
        -> yarnData.copy(quality = param as String)

        YarnParamName.WEIGHT
        -> yarnData.copy(weight = ((param as String).ifBlank { null })?.toDouble())

        YarnParamName.ROLL
        -> yarnData.copy(roll = param as YarnRoll)

        YarnParamName.LENGTH
        -> yarnData.copy(length = ((param as String).ifBlank { null })?.toDouble())

        YarnParamName.GAUGE_COLUMN_FROM
        -> yarnData.copy(gaugeColumnFrom = ((param as String).ifBlank { null })?.toDouble())

        YarnParamName.GAUGE_COLUMN_TO
        -> yarnData.copy(gaugeColumnTo = ((param as String).ifBlank { null })?.toDouble())

        YarnParamName.GAUGE_ROW_FROM
        -> yarnData.copy(gaugeRowFrom = ((param as String).ifBlank { null })?.toDouble())

        YarnParamName.GAUGE_ROW_TO
        -> yarnData.copy(gaugeRowTo = ((param as String).ifBlank { null })?.toDouble())

        YarnParamName.GAUGE_STITCH
        -> yarnData.copy(gaugeStitch = param as String)

        YarnParamName.NEEDLE_SIZE_FROM
        -> yarnData.copy(needleSizeFrom = ((param as String).ifBlank { null })?.toDouble())

        YarnParamName.NEEDLE_SIZE_TO
        -> yarnData.copy(needleSizeTo = ((param as String).ifBlank { null })?.toDouble())

        YarnParamName.CROCHET_NEEDLE_SIZE_FROM
        -> yarnData.copy(crochetNeedleSizeFrom = ((param as String).ifBlank { null })?.toDouble())

        YarnParamName.CROCHET_NEEDLE_SIZE_TO
        -> yarnData.copy(crochetNeedleSizeTo = ((param as String).ifBlank { null })?.toDouble())

        YarnParamName.THICKNESS
        -> yarnData.copy(thickness = param as YarnThickness)

        YarnParamName.YARN_DESCRIPTION
        -> yarnData.copy(yarnDescription = param as String)

        else
        -> yarnData
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
        weight = yarnData.weight,
        roll = yarnData.roll,
        length = yarnData.length,
        gaugeColumnFrom = yarnData.gaugeColumnFrom,
        gaugeColumnTo = yarnData.gaugeColumnTo,
        gaugeRowFrom = yarnData.gaugeRowFrom,
        gaugeRowTo = yarnData.gaugeRowTo,
        gaugeStitch = yarnData.gaugeStitch,
        needleSizeFrom = yarnData.needleSizeFrom,
        needleSizeTo = yarnData.needleSizeTo,
        crochetNeedleSizeFrom = yarnData.crochetNeedleSizeFrom,
        crochetNeedleSizeTo = yarnData.crochetNeedleSizeTo,
        thickness = yarnData.thickness,
        havingNumber = yarnData.havingNumber,
        yarnDescription = yarnData.yarnDescription,
        imageUrl = yarnData.imageUrl,
        drawableResourceId = yarnData.drawableResourceId,
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
        weight = yarnDataForScreen.weight,
        roll = yarnDataForScreen.roll,
        length = yarnDataForScreen.length,
        gaugeColumnFrom = yarnDataForScreen.gaugeColumnFrom,
        gaugeColumnTo = yarnDataForScreen.gaugeColumnTo,
        gaugeRowFrom = yarnDataForScreen.gaugeRowFrom,
        gaugeRowTo = yarnDataForScreen.gaugeRowTo,
        gaugeStitch = yarnDataForScreen.gaugeStitch,
        needleSizeFrom = yarnDataForScreen.needleSizeFrom,
        needleSizeTo = yarnDataForScreen.needleSizeTo,
        crochetNeedleSizeFrom = yarnDataForScreen.crochetNeedleSizeFrom,
        crochetNeedleSizeTo = yarnDataForScreen.crochetNeedleSizeTo,
        thickness = yarnDataForScreen.thickness,
        havingNumber = yarnDataForScreen.havingNumber,
        yarnDescription = yarnDataForScreen.yarnDescription,
        imageUrl = yarnDataForScreen.imageUrl,
        drawableResourceId = yarnDataForScreen.drawableResourceId,
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
        havingNumber = 0,
        yarnDescription = yahooHit.description ?: "",
        imageUrl = yahooHit.image?.medium ?: "",
//        drawableResourceId = yahooHit.drawableResourceId,
    )
}

// バリデーションチェック
fun validateInput(param: Any, paramName: YarnParamName): Boolean {
    return when (paramName) {
        YarnParamName.JAN_CODE
        -> (param as String).length <= YarnParamName.JAN_CODE.maxLength

        YarnParamName.YARN_MAKER_NAME
        -> (param as String).length <= YarnParamName.YARN_MAKER_NAME.maxLength

        YarnParamName.YARN_NAME
        -> (param as String).isNotEmpty() && param.length <= YarnParamName.YARN_NAME.maxLength

        YarnParamName.IMAGE_URL
        -> true

        YarnParamName.DRAWABLE_RESOURCE_ID
        -> true

        YarnParamName.HAVING_NUMBER
        -> (param as Int) <= YarnParamName.HAVING_NUMBER.maxLength

        YarnParamName.COLOR_NUMBER
        -> (param as String).length <= YarnParamName.COLOR_NUMBER.maxLength

        YarnParamName.ROT_NUMBER
        -> (param as String).length <= YarnParamName.ROT_NUMBER.maxLength

        YarnParamName.QUALITY
        -> (param as String).length <= YarnParamName.QUALITY.maxLength

        YarnParamName.WEIGHT
        -> (param as Double) <= YarnParamName.WEIGHT.maxLength.toDouble()

        YarnParamName.ROLL
        -> (param as YarnRoll) in YarnRoll.entries

        YarnParamName.LENGTH
        -> (param as Double) <= YarnParamName.LENGTH.maxLength.toDouble()

        YarnParamName.GAUGE_COLUMN_FROM
        -> (param as Double) <= YarnParamName.GAUGE_COLUMN_FROM.maxLength.toDouble()

        YarnParamName.GAUGE_COLUMN_TO
        -> (param as Double) <= YarnParamName.GAUGE_COLUMN_TO.maxLength.toDouble()

        YarnParamName.GAUGE_ROW_FROM
        -> (param as Double) <= YarnParamName.GAUGE_ROW_FROM.maxLength.toDouble()

        YarnParamName.GAUGE_ROW_TO
        -> (param as Double) <= YarnParamName.GAUGE_ROW_TO.maxLength.toDouble()

        YarnParamName.GAUGE_STITCH
        -> (param as String).length <= YarnParamName.GAUGE_STITCH.maxLength

        YarnParamName.NEEDLE_SIZE_FROM
        -> (param as Double) <= YarnParamName.NEEDLE_SIZE_FROM.maxLength.toDouble()

        YarnParamName.NEEDLE_SIZE_TO
        -> (param as Double) <= YarnParamName.NEEDLE_SIZE_TO.maxLength.toDouble()

        YarnParamName.CROCHET_NEEDLE_SIZE_FROM
        -> (param as Double) <= YarnParamName.CROCHET_NEEDLE_SIZE_FROM.maxLength.toDouble()

        YarnParamName.CROCHET_NEEDLE_SIZE_TO
        -> (param as Double) <= YarnParamName.CROCHET_NEEDLE_SIZE_TO.maxLength.toDouble()

        YarnParamName.THICKNESS
        -> (param as YarnThickness) in YarnThickness.entries

        YarnParamName.YARN_DESCRIPTION
        -> (param as String).length <= YarnParamName.YARN_DESCRIPTION.maxLength

        else
        -> false
    }
}