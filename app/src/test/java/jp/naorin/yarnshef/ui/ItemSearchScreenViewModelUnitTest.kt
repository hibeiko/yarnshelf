package jp.naorin.yarnshef.ui

import jp.naorin.yarnshef.data.YahooShoppingWebServiceItemDummyData
import jp.naorin.yarnshef.dummy.MLKitDummyRepository
import jp.naorin.yarnshef.dummy.YahooShoppingWebServiceItemSearchApiDummyRepository
import jp.naorin.yarnshef.dummy.YarnDataDummyRepository
import jp.naorin.yarnshef.dummy.YarnDummyData
import jp.naorin.yarnshef.rules.TestDispatcherRule
import jp.naorin.yarnshelf.common.YarnParamName
import jp.naorin.yarnshelf.common.YarnRoll
import jp.naorin.yarnshelf.common.YarnThickness
import jp.naorin.yarnshelf.common.yahooHitToYarnDataForScreenConverter
import jp.naorin.yarnshelf.ui.ItemSearchDestination
import jp.naorin.yarnshelf.ui.ItemSearchScreenViewModel
import jp.naorin.yarnshelf.ui.SearchItemUiState
import jp.naorin.yarnshelf.ui.navigation.YarnDataForScreen
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
// 商品検索画面
class ItemSearchScreenViewModelUnitTest {
    // 単体テストが Android UI スレッドに対応していないことです。単体テストは、Android デバイスやエミュレータではなく、ワークステーションで実行されます。ローカル単体テストのコードが Main ディスパッチャを参照する場合、単体テストの実行時に例外（上記の例など）がスローされます。
    // Main ディスパッチャは UI コンテキストでのみ使用できるため、ディスパッチャを単体テストに適したものに置き換える必要があります。Kotlin コルーチン ライブラリには、この目的のために TestDispatcher というコルーチン ディスパッチャが用意されています。
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private lateinit var viewModel: ItemSearchScreenViewModel

    @Before
    fun setUp() {
        viewModel = ItemSearchScreenViewModel(
            yahooShoppingWebServiceItemSearchApiRepository = YahooShoppingWebServiceItemSearchApiDummyRepository(),
            mlKitRepository = MLKitDummyRepository(),
            yarnDataRepository = YarnDataDummyRepository()
        )
    }

    // Destinationのテスト
    @Test
    fun itemSearchScreenViewModel_verifyDestination() {
        assertEquals(ItemSearchDestination.title, "あたらしい毛糸を登録")
        assertEquals(ItemSearchDestination.route, "ItemSearch")
    }

    // バーコードスキャンに成功すると検索結果が取得できること
    @Test
    fun itemSearchScreenViewModel_ScanBarcode_verifySuccess() {
        viewModel.readBarcode()
        assertEquals(
            viewModel.searchItemUiState,
            SearchItemUiState.Success(YahooShoppingWebServiceItemDummyData.dummyData)
        )
    }

    // 検索条件を設定できること
    @Test
    fun itemSearchScreenViewModel_InputSearchCondition_UpdateConditionAndValidateOK() {
        // 初期表示時は検索条件が空でValidationエラーとなる
        assertEquals(
            viewModel.itemSearchScreenUiState.value.yarnNameSearchInput,
            "")
        assertEquals(viewModel.validateYarnNameSearchInput(), false)

        viewModel.yarnNameSearchInputUpdate("ダルマ毛糸")
        assertEquals(
            viewModel.itemSearchScreenUiState.value.yarnNameSearchInput,
            "ダルマ毛糸")
        assertEquals(viewModel.validateYarnNameSearchInput(), true)

        // 検索実施
        viewModel.searchItem("ダルマ毛糸","")
        assertEquals(
            viewModel.searchItemUiState,
            SearchItemUiState.Success(YahooShoppingWebServiceItemDummyData.dummyData)
        )
    }
    // 入力画面に遷移できること
    @Test
    fun itemSearchScreenViewModel_navigateToYarnEntryScreen_SetEditData() {
        // 検索タブ
        viewModel.selectedTabIndexUpdate(1)
        // 検索条件設定
        viewModel.yarnNameSearchInputUpdate("ダルマ毛糸")
        // 検索実施
        viewModel.searchItem("ダルマ毛糸", "")
        // 商品選択
        viewModel.navigateToYarnEntryScreen(
            yahooHitToYarnDataForScreenConverter(
                YahooShoppingWebServiceItemDummyData.dummyData.hits[0]
            )
        )
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData,
            yahooHitToYarnDataForScreenConverter(
                YahooShoppingWebServiceItemDummyData.dummyData.hits[0]
            )
        )
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.isEmpty())
        assertEquals(viewModel.itemSearchScreenUiState.value.entryScreenViewFlag, true)
    }
    // 検索画面に戻れること
    @Test
    fun itemSearchScreenViewModel_BackToItemSearchScreen_ResetEditData() {
        // 検索タブ
        viewModel.selectedTabIndexUpdate(1)
        // 検索条件設定
        viewModel.yarnNameSearchInputUpdate("ダルマ毛糸")
        // 検索実施
        viewModel.searchItem("ダルマ毛糸","")
        // 商品選択
        viewModel.navigateToYarnEntryScreen(
            yahooHitToYarnDataForScreenConverter(
                YahooShoppingWebServiceItemDummyData.dummyData.hits[0]
            )
        )
        // 誤った入力内容をセット
        viewModel.updateYarnEditData("a", YarnParamName.NEEDLE_SIZE_TO)
        // 検索画面に戻る
        viewModel.backToItemSearchScreen()
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData,
            YarnDataForScreen()
        )
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.isEmpty())
        assertEquals(viewModel.itemSearchScreenUiState.value.entryScreenViewFlag, false)
    }

    // 検索に失敗するとエラーが設定されること
    @Test
    fun itemSearchScreenViewModel_SearchErrorOccurred_UpdateUiState() {
        // 検索実施
        viewModel.yarnNameSearchInputUpdate("Error")
        viewModel.searchItem("Error","")
        assertEquals(
            viewModel.searchItemUiState,
            SearchItemUiState.Error
        )
    }

    // 検索タブを変更できること
    @Test
    fun itemSearchScreenViewModel_ChangeTabIndex_UpdateTabAndResetSearchCondition() {
        viewModel.yarnNameSearchInputUpdate("ダルマ毛糸")

        viewModel.selectedTabIndexUpdate(2)
        assertEquals(viewModel.itemSearchScreenUiState.value.selectedTabIndex,2)

        // 検索条件がリセットされる
        assertEquals(
            viewModel.itemSearchScreenUiState.value.yarnNameSearchInput,
            "")
        assertEquals(viewModel.validateYarnNameSearchInput(), false)
        assertEquals(viewModel.searchItemUiState, SearchItemUiState.Loading)
    }

    // 入力値が変更できること。エラーが発生しないこと。
    @Test
    fun itemSearchScreenViewModel_InputJanCode_UpdateJanCodeAndNoError() {
        viewModel.updateYarnEditData("1234567890123", YarnParamName.JAN_CODE)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.janCode, "1234567890123")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.JAN_CODE))
    }

    @Test
    fun itemSearchScreenViewModel_InputMakerName_UpdateMakerNameAndNoError() {
        viewModel.updateYarnEditData("テストメーカー名", YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.yarnMakerName, "テストメーカー名")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
    }

    @Test
    fun itemSearchScreenViewModel_InputYarnName_UpdateYarnNameAndNoError() {
        viewModel.updateYarnEditData("テスト毛糸名", YarnParamName.YARN_NAME)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.yarnName, "テスト毛糸名")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
    }

    @Test
    fun itemSearchScreenViewModel_InputColorNumber_UpdateColorNumberAndNoError() {
        viewModel.updateYarnEditData("テスト色番号", YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.colorNumber, "テスト色番号")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
    }

    @Test
    fun itemSearchScreenViewModel_InputRotNumber_UpdateRotNumberAndNoError() {
        viewModel.updateYarnEditData("テストロット番号", YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.rotNumber, "テストロット番号")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
    }

    @Test
    fun itemSearchScreenViewModel_InputQuality_UpdateQualityAndNoError() {
        viewModel.updateYarnEditData("テスト品質", YarnParamName.QUALITY)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.quality, "テスト品質")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.QUALITY))
    }

    @Test
    fun itemSearchScreenViewModel_InputWeight_UpdateWeightAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.WEIGHT)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.weight, "30.1")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
    }
    @Test
    fun itemSearchScreenViewModel_InputInvalidWeight_UpdateWeightAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.WEIGHT)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.weight, "a")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.WEIGHT],
            "数値を入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_InputRoll_UpdateRollAndNoError() {
        viewModel.updateYarnEditData(YarnRoll.BALL, YarnParamName.ROLL)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.roll, YarnRoll.BALL)
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROLL))
    }

    @Test
    fun itemSearchScreenViewModel_InputLength_UpdateLengthAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.LENGTH)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.length, "100.1")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
    }
    @Test
    fun itemSearchScreenViewModel_InputInvalidLength_UpdateLengthAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.LENGTH)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.length, "a")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.LENGTH],
            "数値を入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_InputGaugeColumnFrom_UpdateGaugeColumnFromAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeColumnFrom, "20.5")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
    }
    @Test
    fun itemSearchScreenViewModel_InputInvalidGaugeColumnFrom_UpdateGaugeColumnFromAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeColumnFrom, "a")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_FROM],
            "数値を入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_InputGaugeColumnTo_UpdateGaugeColumnToAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeColumnTo, "20.5")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
    }
    @Test
    fun itemSearchScreenViewModel_InputInvalidGaugeColumnTo_UpdateGaugeColumnToAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeColumnTo, "a")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_TO],
            "数値を入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_InputGaugeRowFrom_UpdateGaugeRowFromAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeRowFrom, "20.5")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
    }
    @Test
    fun itemSearchScreenViewModel_InputInvalidGaugeRowFrom_UpdateGaugeRowFromAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeRowFrom, "a")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_FROM],
            "数値を入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_InputGaugeRowTo_UpdateGaugeRowToAndNoError() {
        viewModel.updateYarnEditData("20.5", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeRowTo, "20.5")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
    }
    @Test
    fun itemSearchScreenViewModel_InputInvalidGaugeRowTo_UpdateGaugeRowToAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeRowTo, "a")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_TO],
            "数値を入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_InputGaugeStitch_UpdateGaugeStitchAndNoError() {
        viewModel.updateYarnEditData("テストメリヤス編み", YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeStitch, "テストメリヤス編み")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
    }

    @Test
    fun itemSearchScreenViewModel_InputNeedleSizeFrom_UpdateNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.needleSizeFrom, "5.5")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
    }
    @Test
    fun itemSearchScreenViewModel_InputInvalidNeedleSizeFrom_UpdateNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.needleSizeFrom, "a")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_FROM],
            "数値を入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_InputNeedleSizeTo_UpdateNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.needleSizeTo, "5.5")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
    }
    @Test
    fun itemSearchScreenViewModel_InputInvalidNeedleSizeTo_UpdateNeedleSizeToAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.needleSizeTo, "a")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_TO],
            "数値を入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_InputCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.crochetNeedleSizeFrom, "5.5")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
    }
    @Test
    fun itemSearchScreenViewModel_InputInvalidCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.crochetNeedleSizeFrom, "a")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_FROM],
            "数値を入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_InputCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("5.5", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.crochetNeedleSizeTo, "5.5")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
    }
    @Test
    fun itemSearchScreenViewModel_InputInvalidCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.crochetNeedleSizeTo, "a")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_TO],
            "数値を入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_InputThickness_UpdateThicknessAndNoError() {
        viewModel.updateYarnEditData(YarnThickness.NORMAL_THICK, YarnParamName.THICKNESS)
        assertEquals(
            viewModel.itemSearchScreenUiState.value.itemSearchData.thickness,
            YarnThickness.NORMAL_THICK
        )
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.THICKNESS))
    }

    @Test
    fun itemSearchScreenViewModel_InputHavingNumber_UpdateHavingNumberAndNoError() {
        viewModel.updateYarnEditData("10", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.havingNumber, "10")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
    }
    @Test
    fun itemSearchScreenViewModel_InputInvalidHavingNumber_UpdateHavingNumberAndError() {
        viewModel.updateYarnEditData("a", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.havingNumber, "a")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.HAVING_NUMBER],
            "数値を入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_InputYarnDescription_UpdateYarnDescriptionAndNoError() {
        viewModel.updateYarnEditData("テストメモ", YarnParamName.YARN_DESCRIPTION)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.yarnDescription, "テストメモ")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
    }

    @Test
    fun itemSearchScreenViewModel_InputImageUrl_UpdateImageUrlAndNoError() {
        viewModel.updateYarnEditData(
            "https://item-shopping.c.yimg.jp/i/g/shugale1_0089400",
            YarnParamName.IMAGE_URL
        )
        assertEquals(
            viewModel.itemSearchScreenUiState.value.itemSearchData.imageUrl,
            "https://item-shopping.c.yimg.jp/i/g/shugale1_0089400"
        )
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.IMAGE_URL))
    }

    // エラーが発生すること。
    @Test
    fun itemSearchScreenViewModel_InputJanCode_UpdateJanCodeAndError() {
        viewModel.updateYarnEditData("1".repeat(14), YarnParamName.JAN_CODE)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.janCode, "1".repeat(14))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.JAN_CODE))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.JAN_CODE],
            "13桁で入力してください。"
        )
    }

    @Test
    fun itemSearchScreenViewModel_InputMakerName_UpdateMakerNameAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.yarnMakerName, "あ".repeat(101))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.YARN_MAKER_NAME],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun itemSearchScreenViewModel_DeleteYarnName_UpdateYarnNameAndError() {
        viewModel.updateYarnEditData("", YarnParamName.YARN_NAME)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.yarnName, "")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],
            "名前は必須項目です。"
        )
    }

    @Test
    fun itemSearchScreenViewModel_InputYarnName_UpdateYarnNameAndError() {
        viewModel.updateYarnEditData("あ".repeat(201), YarnParamName.YARN_NAME)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.yarnName, "あ".repeat(201))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],
            "200文字以内で入力してください。"
        )
    }

    @Test
    fun itemSearchScreenViewModel_InputColorNumber_UpdateColorNumberAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.colorNumber, "あ".repeat(101))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.COLOR_NUMBER],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun itemSearchScreenViewModel_InputRotNumber_UpdateRotNumberAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.rotNumber, "あ".repeat(101))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.ROT_NUMBER],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun itemSearchScreenViewModel_InputQuality_UpdateQualityAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.QUALITY)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.quality, "あ".repeat(101))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.QUALITY))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.QUALITY],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun itemSearchScreenViewModel_InputWeight_UpdateWeightAndError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.WEIGHT)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.weight, "10000.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.WEIGHT],
            "10000g以内で入力してください。"
        )
    }

    @Test
    fun itemSearchScreenViewModel_DeleteWeight_UpdateWeightAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.WEIGHT)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.weight, "")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
    }

    @Test
    fun itemSearchScreenViewModel_InputLength_UpdateLengthAndError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.LENGTH)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.length, "10000.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.LENGTH],
            "10000m以内で入力してください。"
        )
    }

    @Test
    fun itemSearchScreenViewModel_DeleteLength_UpdateLengthAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.LENGTH)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.length, "")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
    }

    @Test
    fun itemSearchScreenViewModel_InputGaugeColumnFrom_UpdateGaugeColumnFromAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeColumnFrom, "100.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_FROM],
            "100以内で入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_DeleteGaugeColumnFrom_UpdateGaugeColumnFromAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeColumnFrom, "")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
    }
    @Test
    fun itemSearchScreenViewModel_InputGaugeColumnTo_UpdateGaugeColumnToAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeColumnTo, "100.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_TO],
            "100以内で入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_DeleteGaugeColumnTo_UpdateGaugeColumnToAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeColumnTo, "")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
    }
    @Test
    fun itemSearchScreenViewModel_InputGaugeRowFrom_UpdateGaugeRowFromAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeRowFrom, "100.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_FROM],
            "100以内で入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_DeleteGaugeRowFrom_UpdateGaugeRowFromAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeRowFrom, "")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
    }
    @Test
    fun itemSearchScreenViewModel_InputGaugeRowTo_UpdateGaugeRowToAndError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeRowTo, "100.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_TO],
            "100以内で入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_DeleteGaugeRowTo_UpdateGaugeRowToAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeRowTo, "")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
    }
    @Test
    fun itemSearchScreenViewModel_InputGaugeStitch_UpdateGaugeStitchAndError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeStitch, "あ".repeat(101))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_STITCH],
            "100文字以内で入力してください。"
        )
    }

    @Test
    fun itemSearchScreenViewModel_InputNeedleSizeFrom_UpdateNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.needleSizeFrom, "30.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_FROM],
            "30以内で入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_DeleteNeedleSizeFrom_UpdateNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.needleSizeFrom, "")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
    }
    @Test
    fun itemSearchScreenViewModel_InputNeedleSizeTo_UpdateNeedleSizeToAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.needleSizeTo, "30.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_TO],
            "30以内で入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_DeleteNeedleSizeTo_UpdateNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.needleSizeTo, "")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
    }
    @Test
    fun itemSearchScreenViewModel_InputCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.crochetNeedleSizeFrom, "30.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_FROM],
            "30以内で入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_DeleteCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.crochetNeedleSizeFrom, "")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
    }
    @Test
    fun itemSearchScreenViewModel_InputCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.crochetNeedleSizeTo, "30.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_TO],
            "30以内で入力してください。"
        )
    }
    @Test
    fun itemSearchScreenViewModel_DeleteCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.crochetNeedleSizeTo, "")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
    }
    @Test
    fun itemSearchScreenViewModel_InputHavingNumber_UpdateHavingNumberAndError() {
        viewModel.updateYarnEditData("1001", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.havingNumber, "1001")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.HAVING_NUMBER],
            "1000個以内で入力してください。"
        )
    }

    @Test
    fun itemSearchScreenViewModel_DeleteHavingNumber_UpdateHavingNumberAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.havingNumber, "0")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
    }

    @Test
    fun itemSearchScreenViewModel_InputYarnDescription_UpdateYarnDescriptionAndError() {
        viewModel.updateYarnEditData("あ".repeat(1001), YarnParamName.YARN_DESCRIPTION)
        assertEquals(
            viewModel.itemSearchScreenUiState.value.itemSearchData.yarnDescription,
            "あ".repeat(1001)
        )
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.YARN_DESCRIPTION],
            "1000文字以内で入力してください。"
        )
    }

    // エラーが発生した後、入力内容を修正するとエラーが解消すること。
    @Test
    fun itemSearchScreenViewModel_FixJanCode_UpdateJanCodeAndNoError() {
        viewModel.updateYarnEditData("1".repeat(14), YarnParamName.JAN_CODE)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.janCode, "1".repeat(14))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.JAN_CODE))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.JAN_CODE],
            "13桁で入力してください。"
        )

        viewModel.updateYarnEditData("1".repeat(13), YarnParamName.JAN_CODE)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.janCode, "1".repeat(13))
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.JAN_CODE))
    }

    @Test
    fun itemSearchScreenViewModel_FixMakerName_UpdateMakerNameAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.yarnMakerName, "あ".repeat(101))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.YARN_MAKER_NAME],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.YARN_MAKER_NAME)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.yarnMakerName, "あ".repeat(100))
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_MAKER_NAME))
    }

    @Test
    fun itemSearchScreenViewModel_DeleteYarnName_UpdateYarnNameAndNoError() {
        viewModel.updateYarnEditData("", YarnParamName.YARN_NAME)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.yarnName, "")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],
            "名前は必須項目です。"
        )

        viewModel.updateYarnEditData("あ", YarnParamName.YARN_NAME)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.yarnName, "あ")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
    }

    @Test
    fun itemSearchScreenViewModel_FixYarnName_UpdateYarnNameAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(201), YarnParamName.YARN_NAME)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.yarnName, "あ".repeat(201))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.YARN_NAME],
            "200文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(200), YarnParamName.YARN_NAME)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.yarnName, "あ".repeat(200))
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_NAME))
    }

    @Test
    fun itemSearchScreenViewModel_FixColorNumber_UpdateColorNumberAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.colorNumber, "あ".repeat(101))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.COLOR_NUMBER],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.COLOR_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.colorNumber, "あ".repeat(100))
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.COLOR_NUMBER))
    }

    @Test
    fun itemSearchScreenViewModel_FixRotNumber_UpdateRotNumberAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.rotNumber, "あ".repeat(101))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.ROT_NUMBER],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.ROT_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.rotNumber, "あ".repeat(100))
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.ROT_NUMBER))
    }

    @Test
    fun itemSearchScreenViewModel_FixQuality_UpdateQualityAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.QUALITY)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.quality, "あ".repeat(101))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.QUALITY))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.QUALITY],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.QUALITY)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.quality, "あ".repeat(100))
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.QUALITY))
    }

    @Test
    fun itemSearchScreenViewModel_FixWeight_UpdateWeightAndNoError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.WEIGHT)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.weight, "10000.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.WEIGHT],
            "10000g以内で入力してください。"
        )

        viewModel.updateYarnEditData("10000.0", YarnParamName.WEIGHT)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.weight, "10000.0")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.WEIGHT))
    }

    @Test
    fun itemSearchScreenViewModel_FixLength_UpdateLengthAndNoError() {
        viewModel.updateYarnEditData("10000.1", YarnParamName.LENGTH)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.length, "10000.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.LENGTH],
            "10000m以内で入力してください。"
        )

        viewModel.updateYarnEditData("10000.0", YarnParamName.LENGTH)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.length, "10000.0")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.LENGTH))
    }

    @Test
    fun itemSearchScreenViewModel_FixGaugeColumnFrom_UpdateGaugeColumnFromAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeColumnFrom, "100.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_FROM],
            "100以内で入力してください。"
        )

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_COLUMN_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeColumnFrom, "100.0")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_FROM))
    }

    @Test
    fun itemSearchScreenViewModel_FixGaugeColumnTo_UpdateGaugeColumnToAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeColumnTo, "100.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_COLUMN_TO],
            "100以内で入力してください。"
        )

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_COLUMN_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeColumnTo, "100.0")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_COLUMN_TO))
    }

    @Test
    fun itemSearchScreenViewModel_FixGaugeRowFrom_UpdateGaugeRowFromAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeRowFrom, "100.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_FROM],
            "100以内で入力してください。"
        )

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_ROW_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeRowFrom, "100.0")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_FROM))
    }

    @Test
    fun itemSearchScreenViewModel_FixGaugeRowTo_UpdateGaugeRowToAndNoError() {
        viewModel.updateYarnEditData("100.1", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeRowTo, "100.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_ROW_TO],
            "100以内で入力してください。"
        )

        viewModel.updateYarnEditData("100.0", YarnParamName.GAUGE_ROW_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeRowTo, "100.0")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_ROW_TO))
    }

    @Test
    fun itemSearchScreenViewModel_FixGaugeStitch_UpdateGaugeStitchAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(101), YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeStitch, "あ".repeat(101))
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.GAUGE_STITCH],
            "100文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(100), YarnParamName.GAUGE_STITCH)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.gaugeStitch, "あ".repeat(100))
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.GAUGE_STITCH))
    }

    @Test
    fun itemSearchScreenViewModel_FixNeedleSizeFrom_UpdateNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.needleSizeFrom, "30.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_FROM],
            "30以内で入力してください。"
        )

        viewModel.updateYarnEditData("30.0", YarnParamName.NEEDLE_SIZE_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.needleSizeFrom, "30.0")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_FROM))
    }

    @Test
    fun itemSearchScreenViewModel_FixNeedleSizeTo_UpdateNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.needleSizeTo, "30.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.NEEDLE_SIZE_TO],
            "30以内で入力してください。"
        )

        viewModel.updateYarnEditData("30.0", YarnParamName.NEEDLE_SIZE_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.needleSizeTo, "30.0")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.NEEDLE_SIZE_TO))
    }

    @Test
    fun itemSearchScreenViewModel_FixCrochetNeedleSizeFrom_UpdateCrochetNeedleSizeFromAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.crochetNeedleSizeFrom, "30.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_FROM],
            "30以内で入力してください。"
        )

        viewModel.updateYarnEditData("30.0", YarnParamName.CROCHET_NEEDLE_SIZE_FROM)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.crochetNeedleSizeFrom, "30.0")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_FROM))
    }

    @Test
    fun itemSearchScreenViewModel_FixCrochetNeedleSizeTo_UpdateCrochetNeedleSizeToAndNoError() {
        viewModel.updateYarnEditData("30.1", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.crochetNeedleSizeTo, "30.1")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.CROCHET_NEEDLE_SIZE_TO],
            "30以内で入力してください。"
        )

        viewModel.updateYarnEditData("30.0", YarnParamName.CROCHET_NEEDLE_SIZE_TO)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.crochetNeedleSizeTo, "30.0")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.CROCHET_NEEDLE_SIZE_TO))
    }

    @Test
    fun itemSearchScreenViewModel_FixHavingNumber_UpdateHavingNumberAndNoError() {
        viewModel.updateYarnEditData("1001", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.havingNumber, "1001")
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.HAVING_NUMBER],
            "1000個以内で入力してください。"
        )

        viewModel.updateYarnEditData("1000", YarnParamName.HAVING_NUMBER)
        assertEquals(viewModel.itemSearchScreenUiState.value.itemSearchData.havingNumber, "1000")
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.HAVING_NUMBER))
    }

    @Test
    fun itemSearchScreenViewModel_FixYarnDescription_UpdateYarnDescriptionAndNoError() {
        viewModel.updateYarnEditData("あ".repeat(1001), YarnParamName.YARN_DESCRIPTION)
        assertEquals(
            viewModel.itemSearchScreenUiState.value.itemSearchData.yarnDescription,
            "あ".repeat(1001)
        )
        assertTrue(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
        assertEquals(
            viewModel.itemSearchScreenUiState.value.isErrorMap[YarnParamName.YARN_DESCRIPTION],
            "1000文字以内で入力してください。"
        )

        viewModel.updateYarnEditData("あ".repeat(1000), YarnParamName.YARN_DESCRIPTION)
        assertEquals(
            viewModel.itemSearchScreenUiState.value.itemSearchData.yarnDescription,
            "あ".repeat(1000)
        )
        assertFalse(viewModel.itemSearchScreenUiState.value.isErrorMap.containsKey(YarnParamName.YARN_DESCRIPTION))
    }
    // 毛糸情報が更新されること
    @Test
    fun itemSearchScreenViewModel_UpdateYarnData_verifySuccess() {
        // 商品選択
        viewModel.navigateToYarnEntryScreen(
            yahooHitToYarnDataForScreenConverter(
                YahooShoppingWebServiceItemDummyData.dummyData.hits[0]
            )
        )
        viewModel.updateYarnData()
        assertEquals(
            YahooShoppingWebServiceItemDummyData.dummyData.hits[0].name,
            YarnDummyData.dummyDataList.last().yarnName
        )
//        assertEquals(
//            yarnDataForScreenToYarnDataConverter( yahooHitToYarnDataForScreenConverter(
//                YahooShoppingWebServiceItemDummyData.dummyData.hits[0]
//            )),
//            YarnDummyData.dummyDataList.last()
//        )
    }
}
