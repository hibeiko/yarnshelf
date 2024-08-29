package jp.naorin.yarnshef.ui

import androidx.lifecycle.SavedStateHandle
import jp.naorin.yarnshef.dummy.YarnDataDummyRepository
import jp.naorin.yarnshef.dummy.YarnDummyData
import jp.naorin.yarnshef.rules.TestDispatcherRule
import jp.naorin.yarnshelf.common.YarnParamName
import jp.naorin.yarnshelf.common.YarnRoll
import jp.naorin.yarnshelf.common.YarnThickness
import jp.naorin.yarnshelf.common.yarnDataForScreenToYarnDataConverter
import jp.naorin.yarnshelf.common.yarnDataToYarnDataForScreenConverter
import jp.naorin.yarnshelf.ui.YarnEditDestination
import jp.naorin.yarnshelf.ui.YarnEditScreenViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
// 毛糸情報編集画面
class YarnEditScreenViewModelUnitTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var viewModel: YarnEditScreenViewModel
    private lateinit var repository: YarnDataDummyRepository

    @Before
    fun setUp() {
        repository = YarnDataDummyRepository()
        val savedStateHandle = SavedStateHandle(mapOf(YarnEditDestination.yarnIdArg to 1))
        viewModel = YarnEditScreenViewModel(
            savedStateHandle = savedStateHandle,
            yarnDataRepository = repository,
        )
    }

    // Destinationのテスト
    @Test
    fun yarnEditScreenViewModel_verifyDestination() {
        assertEquals(YarnEditDestination.title, "けいとを編集")
        assertEquals(YarnEditDestination.route, "YarnInfoEdit")
    }

    // 初期表示状態のテスト
    @Test
    fun yarnEditScreenViewModel_InitialState_NothingInput() = runTest{
        repository.emitSelect(1)

        // 初期設定されること
        assertEquals(
            yarnDataToYarnDataForScreenConverter( YarnDummyData.dummyDataList.first { it.yarnId == 1 }),
            viewModel.yarnEditScreenUiState.yarnEditData
        )

        // エラーが発生していないこと
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.isEmpty())
    }

    // 入力値が変更できること。エラーが発生しないこと。
    @Test
    fun yarnEditScreenViewModel_InputJanCode_UpdateJanCodeAndNoError() {
        viewModel.updateYarnEditData("1234567890123", YarnParamName.JAN_CODE)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.janCode, "1234567890123")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.JAN_CODE))
    }

    @Test
    fun yarnEditScreenViewModel_InputMakerName_UpdateMakerNameAndNoError() {
        viewModel.updateYarnEditData("テストメーカー名", YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.yarnMakerName, "テストメーカー名")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
    }

    @Test
    fun yarnEditScreenViewModel_InputYarnName_UpdateYarnNameAndNoError() {
        viewModel.updateYarnEditData("テスト毛糸名", YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.yarnName, "テスト毛糸名")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_NAME))
    }

    @Test
    fun yarnEditScreenViewModel_InputColorNumber_UpdateColorNumberAndNoError() {
        viewModel.updateYarnEditData("テスト色番号", YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.colorNumber, "テスト色番号")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
    }

    @Test
    fun yarnEditScreenViewModel_InputRotNumber_UpdateRotNumberAndNoError() {
        viewModel.updateYarnEditData("テストロット番号", YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.rotNumber, "テストロット番号")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
    }

    @Test
    fun yarnEditScreenViewModel_InputQuality_UpdateQualityAndNoError() {
        viewModel.updateYarnEditData("テスト品質", YarnParamName.QUALITY)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.quality, "テスト品質")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.QUALITY))
    }

    @Test
    fun yarnEditScreenViewModel_InputWeight_UpdateWeightAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.weight, "30.1")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.WEIGHT))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidWeight_UpdateWeightAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.weight, "a")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.WEIGHT))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.WEIGHT],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputRoll_UpdateRollAndNoError() {
        viewModel.updateYarnEditData(YarnRoll.BALL, YarnParamName.ROLL)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.roll, YarnRoll.BALL)
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.ROLL))
    }

    @Test
    fun yarnEditScreenViewModel_InputLength_UpdateLengthAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.length, "100.1")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.LENGTH))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidLength_UpdateLengthAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.length, "a")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.LENGTH))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.LENGTH],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeColumnFrom_UpdateGaugeColumnFromAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeColumnFrom, "20.5")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidGaugeColumnFrom_UpdateGaugeColumnFromAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeColumnFrom, "a")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_COLUMN_FROM],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeColumnTo_UpdateGaugeColumnToAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeColumnTo, "20.5")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidGaugeColumnTo_UpdateGaugeColumnToAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeColumnTo, "a")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_COLUMN_TO],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeRowFrom_UpdateGaugeRowFromAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeRowFrom, "20.5")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidGaugeRowFrom_UpdateGaugeRowFromAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeRowFrom, "a")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_ROW_FROM],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeRowTo_UpdateGaugeRowToAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeRowTo, "20.5")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidGaugeRowTo_UpdateGaugeRowToAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeRowTo, "a")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_ROW_TO],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeStitch_UpdateGaugeStitchAndNoError() {
        viewModel.updateYarnEditData("テストメリヤス編み", YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeStitch, "テストメリヤス編み")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
    }

    @Test
    fun yarnEditScreenViewModel_InputNeedleSizeFrom_UpdateNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.needleSizeFrom, "5.5")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidNeedleSizeFrom_UpdateNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.needleSizeFrom, "a")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.NEEDLE_SIZE_FROM],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputNeedleSizeTo_UpdateNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.needleSizeTo, "5.5")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidNeedleSizeTo_UpdateNeedleSizeToAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.needleSizeTo, "a")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.NEEDLE_SIZE_TO],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.crochetNeedleSizeFrom, "5.5")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.crochetNeedleSizeFrom, "a")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_FROM],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.crochetNeedleSizeTo, "5.5")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.crochetNeedleSizeTo, "a")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_TO],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputThickness_UpdateThicknessAndNoError() {
        viewModel.updateYarnEditData(YarnThickness.NORMAL_THICK, YarnParamName.THICKNESS)
        assertEquals(
            viewModel.yarnEditScreenUiState.yarnEditData.thickness,
            YarnThickness.NORMAL_THICK
        )
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.THICKNESS))
    }

    @Test
    fun yarnEditScreenViewModel_InputHavingNumber_UpdateHavingNumberAndNoError() {
        viewModel.updateYarnEditData("10", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.havingNumber, "10")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
    }
    @Test
    fun yarnEditScreenViewModel_InputInvalidHavingNumber_UpdateHavingNumberAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.havingNumber, "a")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.HAVING_NUMBER],
            "数値を入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_InputYarnDescription_UpdateYarnDescriptionAndNoError() {
        viewModel.updateYarnEditData("テストメモ", YarnParamName.YARN_DESCRIPTION)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.yarnDescription, "テストメモ")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
    }

    @Test
    fun yarnEditScreenViewModel_InputImageUrl_UpdateImageUrlAndNoError() {
        viewModel.updateYarnEditData(
            "https://item-shopping.c.yimg.jp/i/g/shugale1_0089400",
            YarnParamName.IMAGE_URL
        )
        assertEquals(
            viewModel.yarnEditScreenUiState.yarnEditData.imageUrl,
            "https://item-shopping.c.yimg.jp/i/g/shugale1_0089400"
        )
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.IMAGE_URL))
    }

    // エラーが発生すること。
    @Test
    fun yarnEditScreenViewModel_InputJanCode_UpdateJanCodeAndError() {
        viewModel.updateYarnEditData("1".repeat(14), YarnParamName.JAN_CODE)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.janCode, "1".repeat(14))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.JAN_CODE))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.JAN_CODE],
            "13桁で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputMakerName_UpdateMakerNameAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.yarnMakerName, "あ".repeat(101))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.YARN_MAKER_NAME],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_DeleteYarnName_UpdateYarnNameAndError() {
        viewModel.updateYarnEditData("", YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.yarnName, "")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.YARN_NAME],
            "名前は必須項目です。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputYarnName_UpdateYarnNameAndError() {
        viewModel.updateYarnEditData("あ".repeat(201), YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.yarnName, "あ".repeat(201))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.YARN_NAME],
            "200文字以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputColorNumber_UpdateColorNumberAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.colorNumber, "あ".repeat(101))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.COLOR_NUMBER],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputRotNumber_UpdateRotNumberAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.rotNumber, "あ".repeat(101))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.ROT_NUMBER],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputQuality_UpdateQualityAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.QUALITY)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.quality, "あ".repeat(101))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.QUALITY))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.QUALITY],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputWeight_UpdateWeightAndError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.weight, "10000.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.WEIGHT))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.WEIGHT],
            "10000g以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_DeleteWeight_UpdateWeightAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.weight, "")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.WEIGHT))
    }

    @Test
    fun yarnEditScreenViewModel_InputLength_UpdateLengthAndError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.length, "10000.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.LENGTH))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.LENGTH],
            "10000m以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_DeleteLength_UpdateLengthAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.length, "")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.LENGTH))
    }

    @Test
    fun yarnEditScreenViewModel_InputGaugeColumnFrom_UpdateGaugeColumnFromAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeColumnFrom, "100.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_COLUMN_FROM],
            "100以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteGaugeColumnFrom_UpdateGaugeColumnFromAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeColumnFrom, "")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeColumnTo_UpdateGaugeColumnToAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeColumnTo, "100.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_COLUMN_TO],
            "100以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteGaugeColumnTo_UpdateGaugeColumnToAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeColumnTo, "")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeRowFrom_UpdateGaugeRowFromAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeRowFrom, "100.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_ROW_FROM],
            "100以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteGaugeRowFrom_UpdateGaugeRowFromAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeRowFrom, "")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeRowTo_UpdateGaugeRowToAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeRowTo, "100.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_ROW_TO],
            "100以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteGaugeRowTo_UpdateGaugeRowToAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeRowTo, "")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputGaugeStitch_UpdateGaugeStitchAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeStitch, "あ".repeat(101))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_STITCH],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_InputNeedleSizeFrom_UpdateNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.needleSizeFrom, "30.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.NEEDLE_SIZE_FROM],
            "30以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteNeedleSizeFrom_UpdateNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.needleSizeFrom, "")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputNeedleSizeTo_UpdateNeedleSizeToAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.needleSizeTo, "30.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.NEEDLE_SIZE_TO],
            "30以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteNeedleSizeTo_UpdateNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.needleSizeTo, "")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.crochetNeedleSizeFrom, "30.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_FROM],
            "30以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.crochetNeedleSizeFrom, "")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
    }
    @Test
    fun yarnEditScreenViewModel_InputCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.crochetNeedleSizeTo, "30.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_TO],
            "30以内で入力してください。"
        )
    }
    @Test
    fun yarnEditScreenViewModel_DeleteCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.crochetNeedleSizeTo, "")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
    }
    @Test
    fun yarnEditScreenViewModel_InputHavingNumber_UpdateHavingNumberAndError() {
        viewModel.updateYarnEditData("1001", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.havingNumber, "1001")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.HAVING_NUMBER],
            "1000個以内で入力してください。"
        )
    }

    @Test
    fun yarnEditScreenViewModel_DeleteHavingNumber_UpdateHavingNumberAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.havingNumber, "0")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
    }

    @Test
    fun yarnEditScreenViewModel_InputYarnDescription_UpdateYarnDescriptionAndError() {
        viewModel.updateYarnEditData("あ".repeat(1001), YarnParamName.YARN_DESCRIPTION)
        assertEquals(
            viewModel.yarnEditScreenUiState.yarnEditData.yarnDescription,
            "あ".repeat(1001)
        )
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.YARN_DESCRIPTION],
            "1000文字以内で入力してください。"
        )
    }

    // エラーが発生した後、入力内容を修正するとエラーが解消すること。
    @Test
    fun yarnEditScreenViewModel_FixJanCode_UpdateJanCodeAndNoError() {
        viewModel.updateYarnEditData("1".repeat(14), YarnParamName.JAN_CODE)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.janCode, "1".repeat(14))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.JAN_CODE))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.JAN_CODE],
            "13桁で入力してください。"
        )

        viewModel.updateYarnEditData("1".repeat(13), YarnParamName.JAN_CODE)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.janCode, "1".repeat(13))
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.JAN_CODE))
    }

    @Test
    fun yarnEditScreenViewModel_FixMakerName_UpdateMakerNameAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.yarnMakerName, "あ".repeat(101))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.YARN_MAKER_NAME],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.yarnMakerName, "あ".repeat(100))
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
    }

    @Test
    fun yarnEditScreenViewModel_DeleteYarnName_UpdateYarnNameAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.yarnName, "")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.YARN_NAME],
            "名前は必須項目です。"
        )

        viewModel.updateYarnEditData("あ", YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.yarnName, "あ")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_NAME))
    }

    @Test
    fun yarnEditScreenViewModel_FixYarnName_UpdateYarnNameAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(201), YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.yarnName, "あ".repeat(201))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.YARN_NAME],
            "200文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(200), YarnParamName.YARN_NAME)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.yarnName, "あ".repeat(200))
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_NAME))
    }

    @Test
    fun yarnEditScreenViewModel_FixColorNumber_UpdateColorNumberAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.colorNumber, "あ".repeat(101))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.COLOR_NUMBER],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.colorNumber, "あ".repeat(100))
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
    }

    @Test
    fun yarnEditScreenViewModel_FixRotNumber_UpdateRotNumberAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.rotNumber, "あ".repeat(101))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.ROT_NUMBER],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.rotNumber, "あ".repeat(100))
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
    }

    @Test
    fun yarnEditScreenViewModel_FixQuality_UpdateQualityAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.QUALITY)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.quality, "あ".repeat(101))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.QUALITY))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.QUALITY],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.QUALITY)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.quality, "あ".repeat(100))
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.QUALITY))
    }

    @Test
    fun yarnEditScreenViewModel_FixWeight_UpdateWeightAndNoError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.weight, "10000.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.WEIGHT))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.WEIGHT],
            "10000g以内で入力してください。"
        )

        viewModel.updateYarnEditData("10000.0", YarnParamName.WEIGHT)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.weight, "10000.0")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.WEIGHT))
    }

    @Test
    fun yarnEditScreenViewModel_FixLength_UpdateLengthAndNoError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.length, "10000.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.LENGTH))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.LENGTH],
            "10000m以内で入力してください。"
        )

        viewModel.updateYarnEditData("10000.0", YarnParamName.LENGTH)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.length, "10000.0")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.LENGTH))
    }

    @Test
    fun yarnEditScreenViewModel_FixGaugeColumnFrom_UpdateGaugeColumnFromAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeColumnFrom, "100.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_COLUMN_FROM],
            "100以内で入力してください。"
        )

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeColumnFrom, "100.0")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
    }

    @Test
    fun yarnEditScreenViewModel_FixGaugeColumnTo_UpdateGaugeColumnToAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeColumnTo, "100.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_COLUMN_TO],
            "100以内で入力してください。"
        )

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeColumnTo, "100.0")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
    }

    @Test
    fun yarnEditScreenViewModel_FixGaugeRowFrom_UpdateGaugeRowFromAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeRowFrom, "100.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_ROW_FROM],
            "100以内で入力してください。"
        )

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeRowFrom, "100.0")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
    }

    @Test
    fun yarnEditScreenViewModel_FixGaugeRowTo_UpdateGaugeRowToAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeRowTo, "100.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_ROW_TO],
            "100以内で入力してください。"
        )

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeRowTo, "100.0")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
    }

    @Test
    fun yarnEditScreenViewModel_FixGaugeStitch_UpdateGaugeStitchAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeStitch, "あ".repeat(101))
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.GAUGE_STITCH],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.gaugeStitch, "あ".repeat(100))
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
    }

    @Test
    fun yarnEditScreenViewModel_FixNeedleSizeFrom_UpdateNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.needleSizeFrom, "30.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.NEEDLE_SIZE_FROM],
            "30以内で入力してください。"
        )

        viewModel.updateYarnEditData("30.0", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.needleSizeFrom, "30.0")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
    }

    @Test
    fun yarnEditScreenViewModel_FixNeedleSizeTo_UpdateNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.needleSizeTo, "30.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.NEEDLE_SIZE_TO],
            "30以内で入力してください。"
        )

        viewModel.updateYarnEditData("30.0", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.needleSizeTo, "30.0")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
    }

    @Test
    fun yarnEditScreenViewModel_FixCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.crochetNeedleSizeFrom, "30.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_FROM],
            "30以内で入力してください。"
        )

        viewModel.updateYarnEditData("30.0", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.crochetNeedleSizeFrom, "30.0")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
    }

    @Test
    fun yarnEditScreenViewModel_FixCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.crochetNeedleSizeTo, "30.1")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_TO],
            "30以内で入力してください。"
        )

        viewModel.updateYarnEditData("30.0", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.crochetNeedleSizeTo, "30.0")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
    }

    @Test
    fun yarnEditScreenViewModel_FixHavingNumber_UpdateHavingNumberAndNoError() {
        viewModel.updateYarnEditData("1001", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.havingNumber, "1001")
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.HAVING_NUMBER],
            "1000個以内で入力してください。"
        )

        viewModel.updateYarnEditData("1000", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.yarnEditScreenUiState.yarnEditData.havingNumber, "1000")
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
    }

    @Test
    fun yarnEditScreenViewModel_FixYarnDescription_UpdateYarnDescriptionAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(1001), YarnParamName.YARN_DESCRIPTION)
        assertEquals(
            viewModel.yarnEditScreenUiState.yarnEditData.yarnDescription,
            "あ".repeat(1001)
        )
        assertTrue(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
        assertEquals(
            viewModel.yarnEditScreenUiState.isErrorMap[YarnParamName.YARN_DESCRIPTION],
            "1000文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(1000), YarnParamName.YARN_DESCRIPTION)
        assertEquals(
            viewModel.yarnEditScreenUiState.yarnEditData.yarnDescription,
            "あ".repeat(1000)
        )
        assertFalse(viewModel.yarnEditScreenUiState.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
    }
    // 毛糸情報が更新されること
    @Test
    fun yarnEditScreenViewModel_UpdateYarnData_verifySuccess() {
        viewModel.updateYarnData()
        assertEquals(
            yarnDataForScreenToYarnDataConverter( viewModel.yarnEditScreenUiState.yarnEditData),
            YarnDummyData.dummyDataList.first { it.yarnId == viewModel.yarnEditScreenUiState.yarnEditData.yarnId })
    }
}