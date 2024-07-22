package jp.hibeiko.yarnshef.ui

import androidx.lifecycle.SavedStateHandle
import jp.hibeiko.yarnshelf.R
import jp.hibeiko.yarnshelf.common.YarnParamName
import jp.hibeiko.yarnshelf.common.YarnRoll
import jp.hibeiko.yarnshelf.common.YarnThickness
import jp.hibeiko.yarnshelf.data.YarnData
import jp.hibeiko.yarnshelf.ui.YarnEntryDestination
import jp.hibeiko.yarnshelf.ui.YarnEntryScreenViewModel
import jp.hibeiko.yarnshelf.ui.navigation.YarnDataForScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
// 毛糸情報入力画面／手入力を選択して遷移した場合
class YarnEntryScreenViewModelUnitTestForManualInput {
    private lateinit var viewModel: YarnEntryScreenViewModel

    @Before
    fun setUp() {
        val savedStateHandle =
            SavedStateHandle(mapOf(YarnEntryDestination.searchItemArg to YarnDataForScreen()))
        viewModel = YarnEntryScreenViewModel(savedStateHandle = savedStateHandle)
    }

    //thingUnderTest_TriggerOfTest_ResultOfTest 形式を使用してテスト関数名を付けています。
    //
    //thingUnderTest = gameViewModel
    //TriggerOfTest = CorrectWordGuessed
    //ResultOfTest = ScoreUpdatedAndErrorFlagUnset
    // 初期表示状態のテスト
    // ゲージ・参考使用針の「～まで」の入力欄が入力不可であること
    // JANコードが入力不可であること
    @Test
    fun yarnEntryScreenViewModel_InitialStateForManualInput_NothingInput() {
        val yarnData: YarnDataForScreen = viewModel.yarnEntryScreenUiState.value.yarnEntryData
        assertEquals(yarnData.yarnId, 0)
        assertEquals(yarnData.janCode, "")
        assertEquals(yarnData.yarnName, "")
        assertEquals(yarnData.yarnMakerName, "")
        assertEquals(yarnData.colorNumber, "")
        assertEquals(yarnData.rotNumber, "")
        assertEquals(yarnData.quality, "")
        assertEquals(yarnData.weight, "")
        // 玉巻orかせのラジオボタンが未選択であること
        assertEquals(yarnData.roll, YarnRoll.NONE)
        assertEquals(yarnData.length, "")
        assertEquals(yarnData.gaugeColumnFrom, "")
        assertEquals(yarnData.gaugeColumnTo, "")
        assertEquals(yarnData.gaugeRowFrom, "")
        assertEquals(yarnData.gaugeRowTo, "")
        assertEquals(yarnData.gaugeStitch, "")
        assertEquals(yarnData.needleSizeFrom, "")
        assertEquals(yarnData.needleSizeTo, "")
        assertEquals(yarnData.crochetNeedleSizeFrom, "")
        assertEquals(yarnData.crochetNeedleSizeTo, "")
        // 糸の太さが未設定であること
        assertEquals(yarnData.thickness, YarnThickness.NONE)
        // 残量が0玉であること
        assertEquals(yarnData.havingNumber, "0")
        assertEquals(yarnData.yarnDescription, "")
        // 画像URLが未設定かつImage Not Foundであること
        assertEquals(yarnData.imageUrl, "")
        assertEquals(yarnData.drawableResourceId, R.drawable.not_found)

        // 名前が未入力のためエラーになっていること
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        // 名前欄のエラーメッセージが想定通りであること。
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],
            "名前は必須項目です。"
        )
    }
}

// 毛糸情報入力画面／バーコードスキャンか商品検索からYahooショッピングの商品を選択して遷移した場合
class YarnEntryScreenViewModelUnitTestForItemSearch {
    private lateinit var viewModel: YarnEntryScreenViewModel

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                YarnEntryDestination.searchItemArg to YarnDataForScreen(
                    yarnId = 0,
                    janCode = "9999999999999",
                    yarnName = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    yarnMakerName = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    colorNumber = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    rotNumber = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    quality = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    weight = "10000.0",
                    roll = YarnRoll.BALL,
                    length = "10000.0",
                    gaugeColumnFrom = "100.0",
                    gaugeColumnTo = "100.0",
                    gaugeRowFrom = "100.0",
                    gaugeRowTo = "100.0",
                    gaugeStitch = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    needleSizeFrom = "30.0",
                    needleSizeTo = "30.0",
                    crochetNeedleSizeFrom = "30.0",
                    crochetNeedleSizeTo = "30.0",
                    thickness = YarnThickness.VERY_THICK,
                    havingNumber = "1000",
                    yarnDescription = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
                    imageUrl = "https://item-shopping.c.yimg.jp/i/g/ko-da_rambouillet-wc",
                    drawableResourceId = R.drawable.not_found,
                )
            )
        )
        viewModel = YarnEntryScreenViewModel(savedStateHandle = savedStateHandle)
    }

    // Destinationのテスト
    @Test
    fun yarnEntryScreenViewModel_verifyDestination() {
        assertEquals(YarnEntryDestination.title, "毛糸情報入力画面")
        assertEquals(YarnEntryDestination.route, "YarnInfoEntry")
    }

    // 初期表示状態のテスト
    @Test
    fun yarnEntryScreenViewModel_InitialStateForItemSearch_NothingInput() {
        // 初期設定した最大値が設定されること
        val yarnData: YarnDataForScreen = viewModel.yarnEntryScreenUiState.value.yarnEntryData
        assertEquals(yarnData.yarnId, 0)
        assertEquals(yarnData.janCode, "9999999999999")
        assertEquals(
            yarnData.yarnName,
            "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０"
        )
        assertEquals(yarnData.yarnMakerName, "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.colorNumber, "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.rotNumber, "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.quality, "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.weight, "10000.0")
        assertEquals(yarnData.roll, YarnRoll.BALL)
        assertEquals(yarnData.length, "10000.0")
        assertEquals(yarnData.gaugeColumnFrom, "100.0")
        assertEquals(yarnData.gaugeColumnTo, "100.0")
        assertEquals(yarnData.gaugeRowFrom, "100.0")
        assertEquals(yarnData.gaugeRowTo, "100.0")
        assertEquals(yarnData.gaugeStitch, "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.needleSizeFrom, "30.0")
        assertEquals(yarnData.needleSizeTo, "30.0")
        assertEquals(yarnData.crochetNeedleSizeFrom, "30.0")
        assertEquals(yarnData.crochetNeedleSizeTo, "30.0")
        assertEquals(yarnData.thickness, YarnThickness.VERY_THICK)
        assertEquals(yarnData.havingNumber, "1000")
        assertEquals(
            yarnData.yarnDescription,
            "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０"
        )
        assertEquals(yarnData.imageUrl, "https://item-shopping.c.yimg.jp/i/g/ko-da_rambouillet-wc")

        // エラーがないこと
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.isEmpty())
    }

    // 入力値が変更できること。エラーが発生しないこと。
    @Test
    fun yarnEditScreenViewModel_InputJanCode_UpdateJanCodeAndNoError() {
        viewModel.updateYarnEditData("1234567890123", YarnParamName.JAN_CODE)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.janCode, "1234567890123")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.JAN_CODE))
    }

    @Test
    fun yarnEditScreenViewModel_InputMakerName_UpdateMakerNameAndNoError() {
        viewModel.updateYarnEditData("テストメーカー名", YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnMakerName, "テストメーカー名")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
    }

    @Test
    fun yarnEditScreenViewModel_InputYarnName_UpdateYarnNameAndNoError() {
        viewModel.updateYarnEditData("テスト毛糸名", YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "テスト毛糸名")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
    }

    @Test
    fun yarnEditScreenViewModel_InputColorNumber_UpdateColorNumberAndNoError() {
        viewModel.updateYarnEditData("テスト色番号", YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.colorNumber, "テスト色番号")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
    }

    @Test
    fun yarnEditScreenViewModel_InputRotNumber_UpdateRotNumberAndNoError() {
        viewModel.updateYarnEditData("テストロット番号", YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.rotNumber, "テストロット番号")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
    }

    @Test
    fun yarnEditScreenViewModel_InputQuality_UpdateQualityAndNoError() {
        viewModel.updateYarnEditData("テスト品質", YarnParamName.QUALITY)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.quality, "テスト品質")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.QUALITY))
    }

    @Test
    fun yarnEditScreenViewModel_InputWeight_UpdateWeightAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.weight, "30.1")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidWeight_UpdateWeightAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.weight, "a")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.WEIGHT],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputRoll_UpdateRollAndNoError() {
        viewModel.updateYarnEditData(YarnRoll.BALL, YarnParamName.ROLL)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.roll, YarnRoll.BALL)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROLL))
    }

    @Test
    fun yarnEditScreenViewModel_InputLength_UpdateLengthAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.length, "100.1")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidLength_UpdateLengthAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.length, "a")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.LENGTH],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeColumnFrom_UpdateGaugeColumnFromAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnFrom, "20.5")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidGaugeColumnFrom_UpdateGaugeColumnFromAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnFrom, "a")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_FROM],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeColumnTo_UpdateGaugeColumnToAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnTo, "20.5")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidGaugeColumnTo_UpdateGaugeColumnToAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnTo, "a")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_TO],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeRowFrom_UpdateGaugeRowFromAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowFrom, "20.5")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidGaugeRowFrom_UpdateGaugeRowFromAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowFrom, "a")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_FROM],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeRowTo_UpdateGaugeRowToAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowTo, "20.5")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidGaugeRowTo_UpdateGaugeRowToAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowTo, "a")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_TO],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeStitch_UpdateGaugeStitchAndNoError() {
        viewModel.updateYarnEditData("テストメリヤス編み", YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeStitch, "テストメリヤス編み")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
    }

    @Test
    fun yarnEditScreenViewModel_InputNeedleSizeFrom_UpdateNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeFrom, "5.5")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidNeedleSizeFrom_UpdateNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeFrom, "a")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_FROM],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputNeedleSizeTo_UpdateNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeTo, "5.5")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidNeedleSizeTo_UpdateNeedleSizeToAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeTo, "a")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_TO],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeFrom, "5.5")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeFrom, "a")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_FROM],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeTo, "5.5")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeTo, "a")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_TO],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputThickness_UpdateThicknessAndNoError() {
        viewModel.updateYarnEditData(YarnThickness.NORMAL_THICK, YarnParamName.THICKNESS)
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.yarnEntryData.thickness,
            YarnThickness.NORMAL_THICK
        )
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.THICKNESS))
    }

    @Test
    fun yarnEditScreenViewModel_InputHavingNumber_UpdateHavingNumberAndNoError() {
        viewModel.updateYarnEditData("10", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.havingNumber, "10")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidHavingNumber_UpdateHavingNumberAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.havingNumber, "a")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.HAVING_NUMBER],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputYarnDescription_UpdateYarnDescriptionAndNoError() {
        viewModel.updateYarnEditData("テストメモ", YarnParamName.YARN_DESCRIPTION)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnDescription, "テストメモ")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
    }

    @Test
    fun yarnEditScreenViewModel_InputImageUrl_UpdateImageUrlAndNoError() {
        viewModel.updateYarnEditData(
            "https://item-shopping.c.yimg.jp/i/g/shugale1_0089400",
            YarnParamName.IMAGE_URL
        )
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.yarnEntryData.imageUrl,
            "https://item-shopping.c.yimg.jp/i/g/shugale1_0089400"
        )
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.IMAGE_URL))
    }

    // エラーが発生すること。
    @Test
    fun yarnEditScreenViewModel_InputJanCode_UpdateJanCodeAndError() {
        viewModel.updateYarnEditData("1".repeat(14), YarnParamName.JAN_CODE)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.janCode, "1".repeat(14))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.JAN_CODE))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.JAN_CODE],
            "13桁で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputMakerName_UpdateMakerNameAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnMakerName, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_MAKER_NAME],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_DeleteYarnName_UpdateYarnNameAndError() {
        viewModel.updateYarnEditData("", YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],
            "名前は必須項目です。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputYarnName_UpdateYarnNameAndError() {
        viewModel.updateYarnEditData("あ".repeat(201), YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "あ".repeat(201))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],
            "200文字以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputColorNumber_UpdateColorNumberAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.colorNumber, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.COLOR_NUMBER],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputRotNumber_UpdateRotNumberAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.rotNumber, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.ROT_NUMBER],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputQuality_UpdateQualityAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.QUALITY)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.quality, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.QUALITY))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.QUALITY],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputWeight_UpdateWeightAndError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.weight, "10000.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.WEIGHT],
            "10000g以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_DeleteWeight_UpdateWeightAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.weight, "")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
    }

    @Test
    fun yarnEditScreenViewModel_InputLength_UpdateLengthAndError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.length, "10000.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.LENGTH],
            "10000m以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_DeleteLength_UpdateLengthAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.length, "")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
    }

    @Test
    fun yarnEditScreenViewModel_InputGaugeColumnFrom_UpdateGaugeColumnFromAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnFrom, "100.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_FROM],
            "100以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteGaugeColumnFrom_UpdateGaugeColumnFromAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnFrom, "")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeColumnTo_UpdateGaugeColumnToAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnTo, "100.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_TO],
            "100以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteGaugeColumnTo_UpdateGaugeColumnToAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnTo, "")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeRowFrom_UpdateGaugeRowFromAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowFrom, "100.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_FROM],
            "100以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteGaugeRowFrom_UpdateGaugeRowFromAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowFrom, "")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeRowTo_UpdateGaugeRowToAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowTo, "100.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_TO],
            "100以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteGaugeRowTo_UpdateGaugeRowToAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowTo, "")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeStitch_UpdateGaugeStitchAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeStitch, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_STITCH],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputNeedleSizeFrom_UpdateNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeFrom, "30.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_FROM],
            "30以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteNeedleSizeFrom_UpdateNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeFrom, "")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputNeedleSizeTo_UpdateNeedleSizeToAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeTo, "30.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_TO],
            "30以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteNeedleSizeTo_UpdateNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeTo, "")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeFrom, "30.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_FROM],
            "30以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeFrom, "")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeTo, "30.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_TO],
            "30以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeTo, "")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputHavingNumber_UpdateHavingNumberAndError() {
        viewModel.updateYarnEditData("1001", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.havingNumber, "1001")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.HAVING_NUMBER],
            "1000個以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_DeleteHavingNumber_UpdateHavingNumberAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.havingNumber, "0")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
    }

    @Test
    fun yarnEditScreenViewModel_InputYarnDescription_UpdateYarnDescriptionAndError() {
        viewModel.updateYarnEditData("あ".repeat(1001), YarnParamName.YARN_DESCRIPTION)
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnDescription,
            "あ".repeat(1001)
        )
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_DESCRIPTION],
            "1000文字以内で入力してください。"
        )
    }

    // エラーが発生した後、入力内容を修正するとエラーが解消すること。
    @Test
    fun yarnEditScreenViewModel_FixJanCode_UpdateJanCodeAndNoError() {
        viewModel.updateYarnEditData("1".repeat(14), YarnParamName.JAN_CODE)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.janCode, "1".repeat(14))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.JAN_CODE))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.JAN_CODE],
            "13桁で入力してください。"
        )

        viewModel.updateYarnEditData("1".repeat(13), YarnParamName.JAN_CODE)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.janCode, "1".repeat(13))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.JAN_CODE))
    }

    @Test
    fun yarnEditScreenViewModel_FixMakerName_UpdateMakerNameAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnMakerName, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_MAKER_NAME],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnMakerName, "あ".repeat(100))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
    }

    @Test
    fun yarnEditScreenViewModel_DeleteYarnName_UpdateYarnNameAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],
            "名前は必須項目です。"
        )

        viewModel.updateYarnEditData("あ", YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "あ")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
    }

    @Test
    fun yarnEditScreenViewModel_FixYarnName_UpdateYarnNameAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(201), YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "あ".repeat(201))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],
            "200文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(200), YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "あ".repeat(200))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
    }

    @Test
    fun yarnEditScreenViewModel_FixColorNumber_UpdateColorNumberAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.colorNumber, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.COLOR_NUMBER],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.colorNumber, "あ".repeat(100))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
    }

    @Test
    fun yarnEditScreenViewModel_FixRotNumber_UpdateRotNumberAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.rotNumber, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.ROT_NUMBER],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.rotNumber, "あ".repeat(100))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
    }

    @Test
    fun yarnEditScreenViewModel_FixQuality_UpdateQualityAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.QUALITY)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.quality, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.QUALITY))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.QUALITY],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.QUALITY)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.quality, "あ".repeat(100))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.QUALITY))
    }

    @Test
    fun yarnEditScreenViewModel_FixWeight_UpdateWeightAndNoError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.weight, "10000.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.WEIGHT],
            "10000g以内で入力してください。"
        )

        viewModel.updateYarnEditData("10000.0", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.weight, "10000.0")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
    }

    @Test
    fun yarnEditScreenViewModel_FixLength_UpdateLengthAndNoError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.length, "10000.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.LENGTH],
            "10000m以内で入力してください。"
        )

        viewModel.updateYarnEditData("10000.0", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.length, "10000.0")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
    }

    @Test
    fun yarnEditScreenViewModel_FixGaugeColumnFrom_UpdateGaugeColumnFromAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnFrom, "100.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_FROM],
            "100以内で入力してください。"
        )

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnFrom, "100.0")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
    }

    @Test
    fun yarnEditScreenViewModel_FixGaugeColumnTo_UpdateGaugeColumnToAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnTo, "100.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_TO],
            "100以内で入力してください。"
        )

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnTo, "100.0")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
    }

    @Test
    fun yarnEditScreenViewModel_FixGaugeRowFrom_UpdateGaugeRowFromAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowFrom, "100.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_FROM],
            "100以内で入力してください。"
        )

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowFrom, "100.0")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
    }

    @Test
    fun yarnEditScreenViewModel_FixGaugeRowTo_UpdateGaugeRowToAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowTo, "100.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_TO],
            "100以内で入力してください。"
        )

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowTo, "100.0")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
    }

    @Test
    fun yarnEditScreenViewModel_FixGaugeStitch_UpdateGaugeStitchAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeStitch, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_STITCH],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeStitch, "あ".repeat(100))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
    }

    @Test
    fun yarnEditScreenViewModel_FixNeedleSizeFrom_UpdateNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeFrom, "30.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_FROM],
            "30以内で入力してください。"
        )

        viewModel.updateYarnEditData("30.0", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeFrom, "30.0")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
    }

    @Test
    fun yarnEditScreenViewModel_FixNeedleSizeTo_UpdateNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeTo, "30.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_TO],
            "30以内で入力してください。"
        )

        viewModel.updateYarnEditData("30.0", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeTo, "30.0")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
    }

    @Test
    fun yarnEditScreenViewModel_FixCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeFrom, "30.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_FROM],
            "30以内で入力してください。"
        )

        viewModel.updateYarnEditData("30.0", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeFrom, "30.0")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
    }

    @Test
    fun yarnEditScreenViewModel_FixCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeTo, "30.1")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_TO],
            "30以内で入力してください。"
        )

        viewModel.updateYarnEditData("30.0", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeTo, "30.0")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
    }

    @Test
    fun yarnEditScreenViewModel_FixHavingNumber_UpdateHavingNumberAndNoError() {
        viewModel.updateYarnEditData("1001", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.havingNumber, "1001")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.HAVING_NUMBER],
            "1000個以内で入力してください。"
        )

        viewModel.updateYarnEditData("1000", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.havingNumber, "1000")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
    }

    @Test
    fun yarnEditScreenViewModel_FixYarnDescription_UpdateYarnDescriptionAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(1001), YarnParamName.YARN_DESCRIPTION)
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnDescription,
            "あ".repeat(1001)
        )
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_DESCRIPTION],
            "1000文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(1000), YarnParamName.YARN_DESCRIPTION)
        assertEquals(
            viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnDescription,
            "あ".repeat(1000)
        )
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
    }

}