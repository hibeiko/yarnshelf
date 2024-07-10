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
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
// 毛糸情報入力画面／手入力を選択して遷移した場合
class YarnEntryScreenViewModelUnitTestForManualInput {
    private lateinit var viewModel: YarnEntryScreenViewModel

    @Before
    fun setUp(){
        val savedStateHandle = SavedStateHandle(mapOf(YarnEntryDestination.searchItemArg to YarnDataForScreen()))
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
        val yarnData: YarnData = viewModel.yarnEntryScreenUiState.value.yarnEntryData
        assertEquals(yarnData.yarnId,0)
        assertEquals(yarnData.janCode,"")
        assertEquals(yarnData.yarnName,"")
        assertEquals(yarnData.yarnMakerName,"")
        assertEquals(yarnData.colorNumber,"")
        assertEquals(yarnData.rotNumber,"")
        assertEquals(yarnData.quality,"")
        assertEquals(yarnData.weight,null)
        // 玉巻orかせのラジオボタンが未選択であること
        assertEquals(yarnData.roll,YarnRoll.NONE)
        assertEquals(yarnData.length,null)
        assertEquals(yarnData.gaugeColumnFrom,null)
        assertEquals(yarnData.gaugeColumnTo,null)
        assertEquals(yarnData.gaugeRowFrom,null)
        assertEquals(yarnData.gaugeRowTo,null)
        assertEquals(yarnData.gaugeStitch,"")
        assertEquals(yarnData.needleSizeFrom,null)
        assertEquals(yarnData.needleSizeTo,null)
        assertEquals(yarnData.crochetNeedleSizeFrom,null)
        assertEquals(yarnData.crochetNeedleSizeTo,null)
        // 糸の太さが未設定であること
        assertEquals(yarnData.thickness,YarnThickness.NONE)
        // 残量が0玉であること
        assertEquals(yarnData.havingNumber,0)
        assertEquals(yarnData.yarnDescription,"")
        // 画像URLが未設定かつImage Not Foundであること
        assertEquals(yarnData.imageUrl,"")
        assertEquals(yarnData.drawableResourceId, R.drawable.not_found)

        // 名前が未入力のためエラーになっていること
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        // 名前欄のエラーメッセージが想定通りであること。
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],"名前は必須項目です。")
    }
}
// 毛糸情報入力画面／バーコードスキャンか商品検索からYahooショッピングの商品を選択して遷移した場合
class YarnEntryScreenViewModelUnitTestForItemSearch {
    private lateinit var viewModel: YarnEntryScreenViewModel

    @Before
    fun setUp(){
        val savedStateHandle = SavedStateHandle(mapOf(YarnEntryDestination.searchItemArg to YarnDataForScreen(
            yarnId = 0,
            janCode = "9999999999999",
            yarnName = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
            yarnMakerName = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
            colorNumber = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
            rotNumber = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
            quality = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
            weight = 10000.0,
            roll = YarnRoll.BALL,
            length = 10000.0,
            gaugeColumnFrom = 100.0,
            gaugeColumnTo = 100.0,
            gaugeRowFrom = 100.0,
            gaugeRowTo = 100.0,
            gaugeStitch = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
            needleSizeFrom = 30.0,
            needleSizeTo = 30.0,
            crochetNeedleSizeFrom = 30.0,
            crochetNeedleSizeTo = 30.0,
            thickness = YarnThickness.VERY_THICK,
            havingNumber = 1000,
            yarnDescription = "１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０",
            imageUrl = "https://item-shopping.c.yimg.jp/i/g/ko-da_rambouillet-wc",
            drawableResourceId = R.drawable.not_found,
        )))
        viewModel = YarnEntryScreenViewModel(savedStateHandle = savedStateHandle)
    }

    // 初期表示状態のテスト
    @Test
    fun yarnEntryScreenViewModel_InitialStateForManualInput_NothingInput() {
        // 初期設定した最大値が設定されること
        val yarnData: YarnData = viewModel.yarnEntryScreenUiState.value.yarnEntryData
        assertEquals(yarnData.yarnId,0)
        assertEquals(yarnData.janCode,"9999999999999")
        assertEquals(yarnData.yarnName,"１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.yarnMakerName,"１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.colorNumber,"１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.rotNumber,"１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.quality,"１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.weight,10000.0)
        assertEquals(yarnData.roll,YarnRoll.BALL)
        assertEquals(yarnData.length,10000.0)
        assertEquals(yarnData.gaugeColumnFrom,100.0)
        assertEquals(yarnData.gaugeColumnTo,100.0)
        assertEquals(yarnData.gaugeRowFrom,100.0)
        assertEquals(yarnData.gaugeRowTo,100.0)
        assertEquals(yarnData.gaugeStitch,"１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.needleSizeFrom,30.0)
        assertEquals(yarnData.needleSizeTo,30.0)
        assertEquals(yarnData.crochetNeedleSizeFrom,30.0)
        assertEquals(yarnData.crochetNeedleSizeTo,30.0)
        assertEquals(yarnData.thickness,YarnThickness.VERY_THICK)
        assertEquals(yarnData.havingNumber,1000)
        assertEquals(yarnData.yarnDescription,"１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０１２３４５６７８９０")
        assertEquals(yarnData.imageUrl,"https://item-shopping.c.yimg.jp/i/g/ko-da_rambouillet-wc")
        assertEquals(yarnData.drawableResourceId, R.drawable.not_found)

        // 名前が未入力のためエラーになっていること
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        // 名前欄のエラーメッセージが想定通りであること。
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],"名前は必須項目です。")
    }
    // 入力値が変更できること。エラーが発生しないこと。
    @Test
    fun yarnEntryScreenViewModel_InputJanCode_UpdateJanCodeAndNoError() {
        viewModel.updateYarnEditData("1234567890123", YarnParamName.JAN_CODE)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.janCode, "1234567890123")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.JAN_CODE))
    }
    @Test
    fun yarnEntryScreenViewModel_InputMakerName_UpdateMakerNameAndNoError() {
        viewModel.updateYarnEditData("テストメーカー名", YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnMakerName, "テストメーカー名")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
    }
    @Test
    fun yarnEntryScreenViewModel_InputYarnName_UpdateYarnNameAndNoError() {
        viewModel.updateYarnEditData("テスト毛糸名", YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "テスト毛糸名")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
    }
    @Test
    fun yarnEntryScreenViewModel_InputColorNumber_UpdateColorNumberAndNoError() {
        viewModel.updateYarnEditData("テスト色番号", YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.colorNumber, "テスト色番号")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
    }
    @Test
    fun yarnEntryScreenViewModel_InputRotNumber_UpdateRotNumberAndNoError() {
        viewModel.updateYarnEditData("テストロット番号", YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.rotNumber, "テストロット番号")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
    }
    @Test
    fun yarnEntryScreenViewModel_InputQuality_UpdateQualityAndNoError() {
        viewModel.updateYarnEditData("テスト品質", YarnParamName.QUALITY)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.quality, "テスト品質")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.QUALITY))
    }
    @Test
    fun yarnEntryScreenViewModel_InputWeight_UpdateWeightAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.weight, 30.1)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
    }
    @Test
    fun yarnEntryScreenViewModel_InputRoll_UpdateRollAndNoError() {
        viewModel.updateYarnEditData(YarnRoll.BALL, YarnParamName.ROLL)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.roll, YarnRoll.BALL)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROLL))
    }
    @Test
    fun yarnEntryScreenViewModel_InputLength_UpdateLengthAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.length, 100.1)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
    }
    @Test
    fun yarnEntryScreenViewModel_InputGaugeColumnFrom_UpdateGaugeColumnFromAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnFrom, 20.5)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
    }
    @Test
    fun yarnEntryScreenViewModel_InputGaugeColumnTo_UpdateGaugeColumnToAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnTo, 20.5)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
    }
    @Test
    fun yarnEntryScreenViewModel_InputGaugeRowFrom_UpdateGaugeRowFromAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowFrom, 20.5)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
    }
    @Test
    fun yarnEntryScreenViewModel_InputGaugeRowTo_UpdateGaugeRowToAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowTo, 20.5)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
    }
    @Test
    fun yarnEntryScreenViewModel_InputGaugeStitch_UpdateGaugeStitchAndNoError() {
        viewModel.updateYarnEditData("テストメリヤス編み", YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeStitch, "テストメリヤス編み")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
    }
    @Test
    fun yarnEntryScreenViewModel_InputNeedleSizeFrom_UpdateNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeFrom, 5.5)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
    }
    @Test
    fun yarnEntryScreenViewModel_InputNeedleSizeTo_UpdateNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeTo, 5.5)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
    }
    @Test
    fun yarnEntryScreenViewModel_InputCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeFrom, 5.5)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
    }
    @Test
    fun yarnEntryScreenViewModel_InputCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeTo, 5.5)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
    }
    @Test
    fun yarnEntryScreenViewModel_InputThickness_UpdateThicknessAndNoError() {
        viewModel.updateYarnEditData(YarnThickness.NORMAL_THICK, YarnParamName.THICKNESS)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.thickness, YarnThickness.NORMAL_THICK)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.THICKNESS))
    }
    @Test
    fun yarnEntryScreenViewModel_InputHavingNumber_UpdateHavingNumberAndNoError() {
        viewModel.updateYarnEditData("10", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.havingNumber, 10)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
    }
    @Test
    fun yarnEntryScreenViewModel_InputYarnDescription_UpdateYarnDescriptionAndNoError() {
        viewModel.updateYarnEditData("テストメモ", YarnParamName.YARN_DESCRIPTION)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnDescription, "テストメモ")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
    }
    @Test
    fun yarnEntryScreenViewModel_InputImageUrl_UpdateImageUrlAndNoError() {
        viewModel.updateYarnEditData("https://item-shopping.c.yimg.jp/i/g/shugale1_0089400", YarnParamName.IMAGE_URL)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.imageUrl, "https://item-shopping.c.yimg.jp/i/g/shugale1_0089400")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.IMAGE_URL))
    }
    // エラーが発生すること。
    @Test
    fun yarnEntryScreenViewModel_InputJanCode_UpdateJanCodeAndError() {
        viewModel.updateYarnEditData("1".repeat(14), YarnParamName.JAN_CODE)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.janCode, "1".repeat(14))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.JAN_CODE))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.JAN_CODE],"13桁で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputMakerName_UpdateMakerNameAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnMakerName, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_MAKER_NAME],"100文字以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_DeleteYarnName_UpdateYarnNameAndError() {
        viewModel.updateYarnEditData("", YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],"名前は必須項目です。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputYarnName_UpdateYarnNameAndError() {
        viewModel.updateYarnEditData("あ".repeat(201), YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "あ".repeat(201))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],"200文字以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputColorNumber_UpdateColorNumberAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.colorNumber, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.COLOR_NUMBER],"100文字以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputRotNumber_UpdateRotNumberAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.rotNumber, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.ROT_NUMBER],"100文字以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputQuality_UpdateQualityAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.QUALITY)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.quality, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.QUALITY))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.QUALITY],"100文字以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputWeight_UpdateWeightAndError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.weight, 10000.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.WEIGHT],"10000g以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_DeleteWeight_UpdateWeightAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.weight, null)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
    }
    @Test
    fun yarnEntryScreenViewModel_InputLength_UpdateLengthAndError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.length, 10000.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.LENGTH],"10000m以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_DeleteLength_UpdateLengthAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.length, null)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
    }
    @Test
    fun yarnEntryScreenViewModel_InputGaugeColumnFrom_UpdateGaugeColumnFromAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnFrom, 100.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_FROM],"100以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputGaugeColumnTo_UpdateGaugeColumnToAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnTo, 100.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_TO],"100以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputGaugeRowFrom_UpdateGaugeRowFromAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowFrom, 100.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_FROM],"100以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputGaugeRowTo_UpdateGaugeRowToAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowTo, 100.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_TO],"100以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputGaugeStitch_UpdateGaugeStitchAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeStitch, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_STITCH],"100文字以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputNeedleSizeFrom_UpdateNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeFrom, 30.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_FROM],"30以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputNeedleSizeTo_UpdateNeedleSizeToAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeTo, 30.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_TO],"30以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeFrom, 30.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_FROM],"30以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeTo, 30.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_TO],"30以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_InputHavingNumber_UpdateHavingNumberAndError() {
        viewModel.updateYarnEditData("1001", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.havingNumber, 1001)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.HAVING_NUMBER],"1000個以内で入力してください。")
    }
    @Test
    fun yarnEntryScreenViewModel_DeleteHavingNumber_UpdateHavingNumberAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.havingNumber, 0)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
    }
    @Test
    fun yarnEntryScreenViewModel_InputYarnDescription_UpdateYarnDescriptionAndError() {
        viewModel.updateYarnEditData("あ".repeat(1001), YarnParamName.YARN_DESCRIPTION)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnDescription, "あ".repeat(1001))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_DESCRIPTION],"1000文字以内で入力してください。")
    }
    // エラーが発生した後、入力内容を修正するとエラーが解消すること。
    @Test
    fun yarnEntryScreenViewModel_FixJanCode_UpdateJanCodeAndNoError() {
        viewModel.updateYarnEditData("1".repeat(14), YarnParamName.JAN_CODE)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.janCode, "1".repeat(14))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.JAN_CODE))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.JAN_CODE],"13桁で入力してください。")

        viewModel.updateYarnEditData("1".repeat(13), YarnParamName.JAN_CODE)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.janCode, "1".repeat(13))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.JAN_CODE))
    }
    @Test
    fun yarnEntryScreenViewModel_FixMakerName_UpdateMakerNameAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnMakerName, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_MAKER_NAME],"100文字以内で入力してください。")

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnMakerName, "あ".repeat(100))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
    }
    @Test
    fun yarnEntryScreenViewModel_DeleteYarnName_UpdateYarnNameAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "")
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],"名前は必須項目です。")

        viewModel.updateYarnEditData("あ", YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "あ")
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
    }
    @Test
    fun yarnEntryScreenViewModel_FixYarnName_UpdateYarnNameAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(201), YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "あ".repeat(201))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],"200文字以内で入力してください。")

        viewModel.updateYarnEditData("あ".repeat(200), YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnName, "あ".repeat(200))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
    }
    @Test
    fun yarnEntryScreenViewModel_FixColorNumber_UpdateColorNumberAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.colorNumber, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.COLOR_NUMBER],"100文字以内で入力してください。")

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.colorNumber, "あ".repeat(100))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
    }
    @Test
    fun yarnEntryScreenViewModel_FixRotNumber_UpdateRotNumberAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.rotNumber, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.ROT_NUMBER],"100文字以内で入力してください。")

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.rotNumber, "あ".repeat(100))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
    }
    @Test
    fun yarnEntryScreenViewModel_FixQuality_UpdateQualityAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.QUALITY)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.quality, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.QUALITY))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.QUALITY],"100文字以内で入力してください。")

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.QUALITY)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.quality, "あ".repeat(100))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.QUALITY))
    }
    @Test
    fun yarnEntryScreenViewModel_FixWeight_UpdateWeightAndNoError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.weight, 10000.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.WEIGHT],"10000g以内で入力してください。")

        viewModel.updateYarnEditData("10000.0", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.weight, 10000.0)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
    }
    @Test
    fun yarnEntryScreenViewModel_FixLength_UpdateLengthAndNoError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.length, 10000.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.LENGTH],"10000m以内で入力してください。")

        viewModel.updateYarnEditData("10000.0", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.length, 10000.0)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
    }
    @Test
    fun yarnEntryScreenViewModel_FixGaugeColumnFrom_UpdateGaugeColumnFromAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnFrom, 100.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_FROM],"100以内で入力してください。")

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnFrom, 100.0)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
    }
    @Test
    fun yarnEntryScreenViewModel_FixGaugeColumnTo_UpdateGaugeColumnToAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnTo, 100.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_TO],"100以内で入力してください。")

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeColumnTo, 100.0)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
    }
    @Test
    fun yarnEntryScreenViewModel_FixGaugeRowFrom_UpdateGaugeRowFromAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowFrom, 100.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_FROM],"100以内で入力してください。")

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowFrom, 100.0)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
    }
    @Test
    fun yarnEntryScreenViewModel_FixGaugeRowTo_UpdateGaugeRowToAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowTo, 100.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_TO],"100以内で入力してください。")

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeRowTo, 100.0)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
    }
    @Test
    fun yarnEntryScreenViewModel_FixGaugeStitch_UpdateGaugeStitchAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeStitch, "あ".repeat(101))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.GAUGE_STITCH],"100文字以内で入力してください。")

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.gaugeStitch, "あ".repeat(100))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
    }
    @Test
    fun yarnEntryScreenViewModel_FixNeedleSizeFrom_UpdateNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeFrom, 30.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_FROM],"30以内で入力してください。")

        viewModel.updateYarnEditData("30.0", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeFrom, 30.0)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
    }
    @Test
    fun yarnEntryScreenViewModel_FixNeedleSizeTo_UpdateNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeTo, 30.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_TO],"30以内で入力してください。")

        viewModel.updateYarnEditData("30.0", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.needleSizeTo, 30.0)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
    }
    @Test
    fun yarnEntryScreenViewModel_FixCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeFrom, 30.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_FROM],"30以内で入力してください。")

        viewModel.updateYarnEditData("30.0", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeFrom, 30.0)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
    }
    @Test
    fun yarnEntryScreenViewModel_FixCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeTo, 30.1)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_TO],"30以内で入力してください。")

        viewModel.updateYarnEditData("30.0", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.crochetNeedleSizeTo, 30.0)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
    }
    @Test
    fun yarnEntryScreenViewModel_FixHavingNumber_UpdateHavingNumberAndNoError() {
        viewModel.updateYarnEditData("1001", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.havingNumber, 1001)
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.HAVING_NUMBER],"1000個以内で入力してください。")

        viewModel.updateYarnEditData("1000", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.havingNumber, 1000)
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
    }
    @Test
    fun yarnEntryScreenViewModel_FixYarnDescription_UpdateYarnDescriptionAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(1001), YarnParamName.YARN_DESCRIPTION)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnDescription, "あ".repeat(1001))
        assertTrue(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
        assertEquals(viewModel.yarnEntryScreenUiState.value.isErrorMap[YarnParamName.YARN_DESCRIPTION],"1000文字以内で入力してください。")

        viewModel.updateYarnEditData("あ".repeat(1000), YarnParamName.YARN_DESCRIPTION)
        assertEquals(viewModel.yarnEntryScreenUiState.value.yarnEntryData.yarnDescription, "あ".repeat(1000))
        assertFalse(viewModel.yarnEntryScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
    }

}